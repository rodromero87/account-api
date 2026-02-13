package com.bank.account.infrastructure.adapter.out.jpa.mapper

import com.bank.account.domain.enums.Currency
import com.bank.account.domain.enums.StatusTransaction
import com.bank.account.domain.enums.TypeTransaction
import com.bank.account.domain.model.Transaction
import com.bank.account.infrastructure.adapter.out.jpa.entity.TransactionEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class TransactionMapperTest {

    @Test
    fun `should map Transaction domain to TransactionEntity`() {
        val id = UUID.randomUUID()
        val accountId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val domain = Transaction(
            id = id,
            accountId = accountId,
            amount = BigDecimal("100.00"),
            currency = Currency.BRL,
            type = TypeTransaction.CREDIT,
            status = StatusTransaction.APPROVED,
            timestamp = now
        )

        val entity = domain.toEntity()

        assertEquals(id, entity.id)
        assertEquals(accountId, entity.accountId)
        assertEquals(BigDecimal("100.00"), entity.amount)
        assertEquals(Currency.BRL, entity.currency)
        assertEquals(TypeTransaction.CREDIT, entity.type)
        assertEquals(StatusTransaction.APPROVED, entity.status)
        assertEquals(now.epochSecond, entity.timestamp.epochSecond)
    }

    @Test
    fun `should map TransactionEntity to Transaction domain`() {
        val id = UUID.randomUUID()
        val accountId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val entity = TransactionEntity(
            id = id,
            accountId = accountId,
            amount = BigDecimal("50.00"),
            currency = Currency.BRL,
            type = TypeTransaction.DEBIT,
            status = StatusTransaction.APPROVED,
            timestamp = now
        )

        val domain = entity.toDomain()

        assertEquals(id, domain.id)
        assertEquals(accountId, domain.accountId)
        assertEquals(BigDecimal("50.00"), domain.amount)
        assertEquals(Currency.BRL, domain.currency)
        assertEquals(TypeTransaction.DEBIT, domain.type)
        assertEquals(StatusTransaction.APPROVED, domain.status)
        assertEquals(now, domain.timestamp)
    }
}
