package com.bank.account.infrastructure.adapter.`in`.http.controller

import com.bank.account.application.dto.SearchTransactionDto
import com.bank.account.application.dto.TransactionDto
import com.bank.account.application.usecase.SearchTransactionUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class TransactionControllerTest {

    private lateinit var searchTransactionUseCase: SearchTransactionUseCase
    private lateinit var controller: TransactionController

    @BeforeEach
    fun setup() {
        searchTransactionUseCase = mockk()
        controller = TransactionController(searchTransactionUseCase)
    }

    @Test
    fun `should return transactions when account exists`() {
        val accountId = UUID.randomUUID()
        val now = LocalDateTime.now()

        val transactionDto = TransactionDto(
            id = UUID.randomUUID(),
            accountId = accountId,
            amount = BigDecimal("50.00"),
            type = "CREDIT",
            currency = "BRL",
            status = "APPROVED",
            creatAt = now)

        val searchDto = SearchTransactionDto(listOf(transactionDto))

        every { searchTransactionUseCase.searchByAccount(accountId) } returns searchDto

        val response = controller.searchByAccount(accountId.toString())

        assertEquals(1, response.transactions.size)
        assertEquals(transactionDto.id, response.transactions.first().id)
        assertEquals(BigDecimal("50.00"), response.transactions.first().amount)

        verify(exactly = 1) { searchTransactionUseCase.searchByAccount(accountId) }
    }

    @Test
    fun `should throw when accountId is invalid UUID`() {
        assertThrows(IllegalArgumentException::class.java) {
            controller.searchByAccount("invalid-uuid")
        }

        verify(exactly = 0) { searchTransactionUseCase.searchByAccount(any()) }
    }

    @Test
    fun `should propagate exception from use case`() {
        val accountId = UUID.randomUUID()

        every { searchTransactionUseCase.searchByAccount(accountId) } throws RuntimeException("db error")

        assertThrows(RuntimeException::class.java) {
            controller.searchByAccount(accountId.toString())
        }

        verify(exactly = 1) { searchTransactionUseCase.searchByAccount(accountId) }
    }
}
