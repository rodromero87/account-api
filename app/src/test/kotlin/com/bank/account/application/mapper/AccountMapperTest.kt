package com.bank.account.application.mapper

import com.bank.account.application.dto.*
import com.bank.account.domain.enums.Currency
import com.bank.account.domain.model.Account
import com.bank.account.domain.model.Balance
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class AccountMapperTest {

    @Test
    fun `should map CreateAccountDto to Account domain`() {
        val id = UUID.randomUUID()
        val owner = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val dto = CreateAccountDto(
            id = id.toString(),
            owner = owner.toString(),
            createdAt = now,
            balance = BalanceDto(
                amount = BigDecimal("100.00"),
                currency = "BRL"
            )
        )

        val domain = dto.toDomain()

        assertEquals(id, domain.id)
        assertEquals(owner, domain.owner)
        assertEquals(now, domain.createAt)
        assertEquals(BigDecimal("100.00"), domain.balance.amount)
        assertEquals(Currency.BRL, domain.balance.currency)
    }

    @Test
    fun `should map BalanceDto to Balance domain`() {
        val dto = BalanceDto(
            amount = BigDecimal("50.00"),
            currency = "BRL"
        )

        val domain = dto.toDomain()

        assertEquals(BigDecimal("50.00"), domain.amount)
        assertEquals(Currency.BRL, domain.currency)
    }

    @Test
    fun `should map Account domain to AccountDto`() {
        val id = UUID.randomUUID()
        val owner = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val domain = Account(
            id = id,
            owner = owner,
            createAt = now,
            updateAt = now,
            balance = Balance(
                amount = BigDecimal("200.00"),
                currency = Currency.BRL
            )
        )

        val dto = domain.toDto()

        assertEquals(id.toString(), dto.id)
        assertEquals(owner.toString(), dto.owner)
        assertEquals(now, dto.createAt)
        assertEquals(now, dto.updateAt)
        assertEquals(BigDecimal("200.00"), dto.balance.amount)
        assertEquals("BRL", dto.balance.currency)
    }

    @Test
    fun `should map Balance domain to BalanceDto`() {
        val domain = Balance(
            amount = BigDecimal("75.00"),
            currency = Currency.BRL
        )

        val dto = domain.toDto()

        assertEquals(BigDecimal("75.00"), dto.amount)
        assertEquals("BRL", dto.currency)
    }

    @Test
    fun `should throw when CreateAccountDto id is invalid UUID`() {
        val dto = CreateAccountDto(
            id = "invalid-uuid",
            owner = UUID.randomUUID().toString(),
            createdAt = Instant.now(),
            balance = BalanceDto(
                amount = BigDecimal("10.00"),
                currency = "BRL"
            )
        )

        assertThrows(IllegalArgumentException::class.java) {
            dto.toDomain()
        }
    }

    @Test
    fun `should throw when BalanceDto currency is invalid`() {
        val dto = BalanceDto(
            amount = BigDecimal("10.00"),
            currency = "INVALID"
        )

        assertThrows(IllegalArgumentException::class.java) {
            dto.toDomain()
        }
    }
}
