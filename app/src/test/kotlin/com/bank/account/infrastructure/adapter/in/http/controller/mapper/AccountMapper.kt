package com.bank.account.infrastructure.adapter.`in`.http.controller.mapper

import com.bank.account.application.dto.AccountDto
import com.bank.account.application.dto.BalanceDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class AccountMapperTest {

    @Test
    fun `should map AccountDto to AccountResponse`() {
        val id = UUID.randomUUID().toString()
        val owner = UUID.randomUUID().toString()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val dto = AccountDto(
            id = id,
            owner = owner,
            createAt = now,
            updateAt = now,
            balance = BalanceDto(
                amount = BigDecimal("100.00"),
                currency = "BRL"
            )
        )

        val response = dto.toResponse()

        assertEquals(id, response.id)
        assertEquals(owner, response.owner)
        assertEquals(now, response.createAt)
        assertEquals(now, response.updateAt)
        assertEquals(BigDecimal("100.00"), response.balance.amount)
        assertEquals("BRL", response.balance.currency)
    }

    @Test
    fun `should map BalanceDto to BalanceResponse`() {
        val dto = BalanceDto(
            amount = BigDecimal("50.00"),
            currency = "USD"
        )

        val response = dto.toResponse()

        assertEquals(BigDecimal("50.00"), response.amount)
        assertEquals("USD", response.currency)
    }
}
