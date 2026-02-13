package com.bank.account.infrastructure.adapter.`in`.http.controller.mapper

import com.bank.account.application.dto.SearchTransactionDto
import com.bank.account.application.dto.TransactionDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class TransactionMapperTest {

    @Test
    fun `should map TransactionDto to TransactionResponse`() {
        val id = UUID.randomUUID()
        val accountId = UUID.randomUUID()
        val now = LocalDateTime.parse("2026-02-13T00:00:00")

        val dto = TransactionDto(
            id = id,
            accountId = accountId,
            type = "CREDIT",
            amount = BigDecimal("100.00"),
            currency = "BRL",
            status = "APPROVED",
            creatAt = now
        )

        val response = dto.toResponse()

        assertEquals(id, response.id)
        assertEquals(accountId, response.accountId)
        assertEquals("CREDIT", response.type)
        assertEquals(BigDecimal("100.00"), response.amount)
        assertEquals("BRL", response.currency)
        assertEquals("APPROVED", response.status)
        assertEquals(now, response.creatAt)
    }

    @Test
    fun `should map SearchTransactionDto to SearchTransactionResponse`() {
        val id = UUID.randomUUID()
        val accountId = UUID.randomUUID()
        val now = LocalDateTime.parse("2026-02-13T00:00:00")

        val transactionDto = TransactionDto(
            id = id,
            accountId = accountId,
            type = "DEBIT",
            amount = BigDecimal("50.00"),
            currency = "USD",
            status = "APPROVED",
            creatAt = now
        )

        val searchDto = SearchTransactionDto(listOf(transactionDto))

        val response = searchDto.toResponse()

        assertEquals(1, response.transactions.size)
        assertEquals(id, response.transactions.first().id)
        assertEquals(accountId, response.transactions.first().accountId)
        assertEquals("DEBIT", response.transactions.first().type)
        assertEquals(BigDecimal("50.00"), response.transactions.first().amount)
        assertEquals("USD", response.transactions.first().currency)
        assertEquals("APPROVED", response.transactions.first().status)
        assertEquals(now, response.transactions.first().creatAt)
    }
}
