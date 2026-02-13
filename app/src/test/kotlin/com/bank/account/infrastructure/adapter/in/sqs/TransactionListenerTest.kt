package com.bank.account.infrastructure.adapter.`in`.sqs

import com.bank.account.application.command.CommandHandler
import com.bank.account.application.dto.TransactionRecord
import com.bank.account.domain.enums.MetricsTransaction
import com.bank.account.domain.out.port.MetricsTransactionPort
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class TransactionListenerTest {

    private lateinit var objectMapper: ObjectMapper
    private lateinit var metricsPort: MetricsTransactionPort
    private lateinit var handler: CommandHandler
    private lateinit var listener: TransactionListener

    @BeforeEach
    fun setup() {
        objectMapper = mockk()
        metricsPort = mockk(relaxed = true) // relaxed pra não precisar stub em void
        handler = mockk(relaxed = true)

        listener = TransactionListener(
            objectMapper = objectMapper,
            metricsPort = metricsPort,
            handler = handler
        )
    }

    @Test
    fun `should process message successfully and record metrics`() {
        val message = """{"any":"json"}"""
        val record = mockk<TransactionRecord>()

        every { objectMapper.readValue(message, TransactionRecord::class.java) } returns record
        every { handler.handle(record) } just Runs

        listener.onMessage(message)

        verify(exactly = 1) { handler.handle(record) }

        verify(exactly = 1) { metricsPort.count(MetricsTransaction.TOTAL_SUCCESS) }
        verify(exactly = 0) { metricsPort.count(MetricsTransaction.TOTAL_FAILED) }

        // sempre no finally
        verify(exactly = 1) { metricsPort.count(MetricsTransaction.TOTAL_RECEIVED) }
        verify(exactly = 1) {
            metricsPort.duration(MetricsTransaction.PROCESSING_TIME, any())
        }

        confirmVerified(handler, metricsPort)
    }

    @Test
    fun `should record failed when json parsing fails, still record received and duration, and rethrow`() {
        val message = """{invalid-json"""
        val parseEx = IllegalArgumentException("bad json")

        every { objectMapper.readValue(message, TransactionRecord::class.java) } throws parseEx

        val thrown = assertThrows(IllegalArgumentException::class.java) {
            listener.onMessage(message)
        }

        // mesma exception (relançada)
        assert(thrown === parseEx)

        verify(exactly = 0) { handler.handle(any()) }

        verify(exactly = 0) { metricsPort.count(MetricsTransaction.TOTAL_SUCCESS) }
        verify(exactly = 1) { metricsPort.count(MetricsTransaction.TOTAL_FAILED) }

        // sempre no finally
        verify(exactly = 1) { metricsPort.count(MetricsTransaction.TOTAL_RECEIVED) }
        verify(exactly = 1) {
            metricsPort.duration(MetricsTransaction.PROCESSING_TIME, any())
        }

        confirmVerified(handler, metricsPort)
    }

    @Test
    fun `should record failed when handler throws, still record received and duration, and rethrow`() {
        val message = """{"any":"json"}"""
        val record = mockk<TransactionRecord>()
        val handlerEx = RuntimeException("boom")

        every { objectMapper.readValue(message, TransactionRecord::class.java) } returns record
        every { handler.handle(record) } throws handlerEx

        val thrown = assertThrows(RuntimeException::class.java) {
            listener.onMessage(message)
        }
        assert(thrown === handlerEx)

        verify(exactly = 1) { handler.handle(record) }

        verify(exactly = 0) { metricsPort.count(MetricsTransaction.TOTAL_SUCCESS) }
        verify(exactly = 1) { metricsPort.count(MetricsTransaction.TOTAL_FAILED) }

        // sempre no finally
        verify(exactly = 1) { metricsPort.count(MetricsTransaction.TOTAL_RECEIVED) }
        verify(exactly = 1) {
            metricsPort.duration(MetricsTransaction.PROCESSING_TIME, any())
        }

        confirmVerified(handler, metricsPort)
    }
}
