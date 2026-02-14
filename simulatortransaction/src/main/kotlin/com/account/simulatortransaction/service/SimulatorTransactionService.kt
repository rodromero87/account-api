package com.account.simulatortransaction.service


import com.account.simulatortransaction.config.SqsQueueResolver
import com.account.simulatortransaction.controller.dto.SimulationProgressResponse
import com.account.simulatortransaction.controller.dto.SimulationRequest
import com.account.simulatortransaction.controller.dto.SimulationResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequest
import software.amazon.awssdk.services.sqs.model.SendMessageBatchRequestEntry
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.ceil
import kotlin.math.min

@Service
class SimulatorTransactionService(
    private val sqs: SqsAsyncClient,
    private val queueResolver: SqsQueueResolver
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private data class State(
        val simulationId: String,
        val total: Int,
        val ratePerMinute: Int,
        val parallelism: Int,
        val startedAt: Long,
        val sent: AtomicLong = AtomicLong(0),
        val failed: AtomicLong = AtomicLong(0),
        @Volatile var status: String = "RUNNING",
        @Volatile var lastError: String? = null
    )

    private val states = ConcurrentHashMap<String, State>()

    fun start(req: SimulationRequest): SimulationResponse {
        val id = UUID.randomUUID().toString()
        val state = State(
            simulationId = id,
            total = req.totalMessages,
            ratePerMinute = req.ratePerMinute,
            parallelism = req.parallelism,
            startedAt = System.currentTimeMillis()
        )
        states[id] = state

        scope.launch {
            runSimulation(req, state)
        }

        return SimulationResponse(
            simulationId = id,
            totalMessages = req.totalMessages,
            ratePerMinute = req.ratePerMinute,
            parallelism = req.parallelism,
            startedAtEpochMs = state.startedAt
        )
    }

    fun progress(simulationId: String): SimulationProgressResponse {
        val st = states[simulationId] ?: return SimulationProgressResponse(
            simulationId = simulationId,
            status = "NOT_FOUND",
            sent = 0,
            failed = 0,
            total = 0,
            startedAtEpochMs = 0,
            lastError = null
        )

        return SimulationProgressResponse(
            simulationId = st.simulationId,
            status = st.status,
            sent = st.sent.get(),
            failed = st.failed.get(),
            total = st.total,
            startedAtEpochMs = st.startedAt,
            lastError = st.lastError
        )
    }

    private suspend fun runSimulation(req: SimulationRequest, st: State) {
        try {
            val queueUrl = queueResolver.resolveQueueUrl()

            // 10k/min => 60000/10000 = 6ms por msg em média
            // vamos trabalhar em "batches" de 10 msgs (SQS batch max=10)
            val batchSize = 10
            val totalBatches = ceil(req.totalMessages / batchSize.toDouble()).toInt()

            // taxa alvo em batches por minuto
            val batchesPerMinute = req.ratePerMinute / batchSize.toDouble()
            val delayMsPerBatch = if (batchesPerMinute <= 0.0) 0L else (60_000.0 / batchesPerMinute).toLong()

            val sem = Semaphore(req.parallelism)

            for (batchIndex in 0 until totalBatches) {
                if (st.status != "RUNNING") break

                val remaining = req.totalMessages - (batchIndex * batchSize)
                val currentBatchSize = min(batchSize, remaining)

                sem.withPermit {
                    // dispara batch em paralelo, mas sem estourar parallelism
                    scope.launch {
                        try {
                            sendBatch(queueUrl, req, currentBatchSize)
                            st.sent.addAndGet(currentBatchSize.toLong())
                        } catch (e: Exception) {
                            st.failed.addAndGet(currentBatchSize.toLong())
                            st.lastError = e.message
                        }
                    }
                }

                // throttle do ritmo (não é perfeito, mas é estável e funciona bem)
                if (delayMsPerBatch > 0) delay(delayMsPerBatch)
            }

            // aguarda os jobs ainda rodando
            // (pequena pausa pra finalizar os últimos batches)
            delay(500)

            st.status = if (st.failed.get() > 0) "FINISHED_WITH_ERRORS" else "FINISHED"
        } catch (e: Exception) {
            st.status = "FAILED"
            st.lastError = e.message
        }
    }

    private fun sendBatch(queueUrl: String, req: SimulationRequest, batchSize: Int) {
        val entries = (0 until batchSize).map { i ->
            val messageId = UUID.randomUUID().toString()
            SendMessageBatchRequestEntry.builder()
                .id("msg-$i-$messageId")
                .messageBody(buildRandomPayload())
                .build()
        }

        val request = SendMessageBatchRequest.builder()
            .queueUrl(queueUrl)
            .entries(entries)
            .build()

        val resp = sqs.sendMessageBatch(request).join()

        if (resp.failed() != null && resp.failed().isNotEmpty()) {
            throw IllegalStateException("SQS batch had failures: ${resp.failed().joinToString { it.message() ?: it.code() }}")
        }
    }

    private fun buildRandomPayload(): String {
        val transactionId = UUID.randomUUID().toString()
        val accountId = UUID.randomUUID().toString()
        val ownerId = UUID.randomUUID().toString()

        val types = listOf("CREDIT", "DEBIT")
        val currencies = listOf("BRL", "USD", "EUR")
        val status = "APPROVED"

        val type = types.random()
        val currency = currencies.random()


        val amount = (1..50_000).random() / 100.0
        val nowEpochSeconds = Instant.now().epochSecond


        val balance = (1..500_000).random() / 100.0

        return """
    {
      "transaction": {
        "id": "$transactionId",
        "type": "$type",
        "amount": $amount,
        "currency": "$currency",
        "status": "$status",
        "timestamp": $nowEpochSeconds
      },
      "account": {
        "id": "$accountId",
        "owner": "$ownerId",
        "created_at": "1634874339",
        "status": "ENABLED",
        "balance": {
          "amount": $balance,
          "currency": "$currency"
        }
      }
    }
    """.trimIndent()
    }

}