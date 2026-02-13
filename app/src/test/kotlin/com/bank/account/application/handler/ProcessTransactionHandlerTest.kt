package com.bank.account.application.handler

import com.bank.account.application.dto.*
import com.bank.account.application.usecase.ProcessNewTransactionUseCase
import com.bank.account.application.usecase.SearchTransactionUseCase
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class ProcessTransactionHandlerTest {

    private lateinit var searchTransactionUseCase: SearchTransactionUseCase
    private lateinit var newTransactionUseCase: ProcessNewTransactionUseCase
    private lateinit var handler: ProcessTransactionHandler

    @BeforeEach
    fun setup() {
        searchTransactionUseCase = mockk()
        newTransactionUseCase = mockk(relaxed = true)
        handler = ProcessTransactionHandler(searchTransactionUseCase, newTransactionUseCase)
    }

    @Test
    fun `should process new transaction when it does not exist`() {
        val txId = UUID.randomUUID()
        val command = transactionRecordWithId(txId.toString())

        every { searchTransactionUseCase.existsTransaction(txId) } returns false
        every { newTransactionUseCase.process(any(), any()) } just Runs

        handler.handle(command)

        verify(exactly = 1) { searchTransactionUseCase.existsTransaction(txId) }
        verify(exactly = 1) { newTransactionUseCase.process(command.transaction, command.account) }

        confirmVerified(searchTransactionUseCase, newTransactionUseCase)
    }

    @Test
    fun `should NOT process when transaction already exists`() {
        val txId = UUID.randomUUID()
        val command = transactionRecordWithId(txId.toString())

        every { searchTransactionUseCase.existsTransaction(txId) } returns true

        handler.handle(command)

        verify(exactly = 1) { searchTransactionUseCase.existsTransaction(txId) }
        verify(exactly = 0) { newTransactionUseCase.process(any(), any()) }

        confirmVerified(searchTransactionUseCase, newTransactionUseCase)
    }

    @Test
    fun `should throw when transaction id is not a valid UUID and should not call use cases`() {
        val command = transactionRecordWithId("not-a-uuid")

        assertThrows(IllegalArgumentException::class.java) {
            handler.handle(command)
        }

        verify(exactly = 0) { searchTransactionUseCase.existsTransaction(any()) }
        verify(exactly = 0) { newTransactionUseCase.process(any(), any()) }

        confirmVerified(searchTransactionUseCase, newTransactionUseCase)
    }

    private fun transactionRecordWithId(id: String): TransactionRecord {
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val transaction = CreateTransactionDto(
            id = id,
            type = "CREDIT",
            amount = BigDecimal("10.00"),
            currency = "BRL",
            status = "NEW",
            timestamp = now
        )

        val account = CreateAccountDto(
            id = UUID.randomUUID().toString(),
            owner = "Rodrigo",
            createdAt = now,
            balance = BalanceDto(
                amount = BigDecimal("0.00"),
                currency = "BRL"
            )
        )

        return TransactionRecord(
            transaction = transaction,
            account = account
        )
    }
}
