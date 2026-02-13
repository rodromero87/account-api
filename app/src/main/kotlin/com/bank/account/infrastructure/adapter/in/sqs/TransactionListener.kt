package com.bank.account.infrastructure.adapter.`in`.sqs

import com.bank.account.application.command.CommandHandler
import com.bank.account.application.dto.TransactionRecord
import com.bank.account.domain.enums.MetricsTransaction
import com.bank.account.domain.out.port.MetricsTransactionPort
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class TransactionListener(
    private val objectMapper: ObjectMapper,
    private val metricsPort: MetricsTransactionPort,
    private val handler: CommandHandler
) {
    private val log = LoggerFactory.getLogger(TransactionListener::class.java)

    @SqsListener("\${app.sqs.transactions-queue}")
    fun onMessage(message: String) {
        val start = System.currentTimeMillis()

        log.info("Processing message: $message", mapOf("event" to "transaction_received"))

        try {
            val transactionRecord =
                objectMapper.readValue(message, TransactionRecord::class.java)

            handler.handle(transactionRecord)
            metricsPort.count(MetricsTransaction.TOTAL_SUCCESS)

        } catch (e: Exception) {
            metricsPort.count(MetricsTransaction.TOTAL_FAILED)
            log.error("Error: ${e.message} for message: $message", mapOf("event" to "transaction_error"), e)
            throw e
        }finally {
            val duration = System.currentTimeMillis() - start
            metricsPort.duration(MetricsTransaction.PROCESSING_TIME, duration)
            metricsPort.count(MetricsTransaction.TOTAL_RECEIVED)
        }
    }
}
