package com.bank.account.infrastructure.adapter.out.jpa.mapper

import com.bank.account.domain.enums.Currency
import com.bank.account.domain.model.Account
import com.bank.account.domain.model.Balance
import com.bank.account.infrastructure.adapter.out.jpa.entity.AccountEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class AccountMapperTest {

    @Test
    fun `should map Account domain to AccountEntity`() {
        val id = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val domain = Account(
            id = id,
            owner = ownerId,
            createAt = now,
            updateAt = now,
            balance = Balance(
                amount = BigDecimal("100.00"),
                currency = Currency.BRL
            )
        )

        val entity = domain.toEntity()

        assertEquals(id, entity.id)
        assertEquals(ownerId, entity.ownerId)
        assertEquals(BigDecimal("100.00"), entity.balanceAmount)
        assertEquals(Currency.BRL, entity.balanceCurrency)
        assertEquals(now, entity.createdAt)
        assertEquals(now, entity.updatedAt)
    }

    @Test
    fun `should map AccountEntity to Account domain`() {
        val id = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val entity = AccountEntity(
            id = id,
            ownerId = ownerId,
            balanceAmount = BigDecimal("200.00"),
            balanceCurrency = Currency.BRL,
            createdAt = now,
            updatedAt = now
        )

        val domain = entity.toDomain()

        assertEquals(id, domain.id)
        assertEquals(ownerId, domain.owner)
        assertEquals(BigDecimal("200.00"), domain.balance.amount)
        assertEquals(Currency.BRL, domain.balance.currency)
        assertEquals(now, domain.createAt)
        assertEquals(now, domain.updateAt)
    }
}
