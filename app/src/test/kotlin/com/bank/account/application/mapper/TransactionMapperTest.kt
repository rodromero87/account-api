package com.bank.account.application.mapper

import com.bank.account.application.dto.CreateTransactionDto
import com.bank.account.domain.enums.Currency
import com.bank.account.domain.enums.StatusTransaction
import com.bank.account.domain.enums.TypeTransaction
import com.bank.account.domain.model.Transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

class TransactionMapperTest {

    @Test
    fun `should map CreateTransactionDto to Transaction domain`() {
        val accountId = UUID.randomUUID()
        val transactionId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val dto = CreateTransactionDto(
            id = transactionId.toString(),
            type = TypeTransaction.CREDIT.name,
            amount = BigDecimal("100.00"),
            currency = Currency.BRL.name,
            status = StatusTransaction.APPROVED.name,
            timestamp = now
        )

        val domain = dto.toDomain(accountId)

        assertEquals(transactionId, domain.id)
        assertEquals(accountId, domain.accountId)
        assertEquals(BigDecimal("100.00"), domain.amount)
        assertEquals(TypeTransaction.CREDIT, domain.type)
        assertEquals(Currency.BRL, domain.currency)
        assertEquals(StatusTransaction.APPROVED, domain.status)
        assertEquals(now, domain.timestamp)
    }

    @Test
    fun `should map Transaction domain to TransactionDto`() {
        val id = UUID.randomUUID()
        val accountId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val domain = Transaction(
            id = id,
            accountId = accountId,
            amount = BigDecimal("50.00"),
            type = TypeTransaction.DEBIT,
            currency = Currency.BRL,
            status = StatusTransaction.APPROVED,
            timestamp = now
        )

        val dto = domain.toDto()

        assertEquals(id, dto.id)
        assertEquals(accountId, dto.accountId)
        assertEquals("DEBIT", dto.type)
        assertEquals("BRL", dto.currency)
        assertEquals("APPROVED", dto.status)

        val expectedDateTime = LocalDateTime.ofInstant(now, ZoneOffset.UTC)
        assertEquals(expectedDateTime, dto.creatAt)
    }

    @Test
    fun `should throw when transaction id is invalid UUID`() {
        val dto = CreateTransactionDto(
            id = "invalid-uuid",
            type = TypeTransaction.CREDIT.name,
            amount = BigDecimal("10.00"),
            currency = Currency.BRL.name,
            status = StatusTransaction.APPROVED.name,
            timestamp = Instant.now()
        )

        assertThrows(IllegalArgumentException::class.java) {
            dto.toDomain(UUID.randomUUID())
        }
    }

    @Test
    fun `should throw when type is invalid`() {
        val dto = CreateTransactionDto(
            id = UUID.randomUUID().toString(),
            type = "INVALID",
            amount = BigDecimal("10.00"),
            currency = Currency.BRL.name,
            status = StatusTransaction.APPROVED.name,
            timestamp = Instant.now()
        )

        assertThrows(IllegalArgumentException::class.java) {
            dto.toDomain(UUID.randomUUID())
        }
    }

    @Test
    fun `should throw when currency is invalid`() {
        val dto = CreateTransactionDto(
            id = UUID.randomUUID().toString(),
            type = TypeTransaction.CREDIT.name,
            amount = BigDecimal("10.00"),
            currency = "INVALID",
            status = StatusTransaction.APPROVED.name,
            timestamp = Instant.now()
        )

        assertThrows(IllegalArgumentException::class.java) {
            dto.toDomain(UUID.randomUUID())
        }
    }

    @Test
    fun `should throw when status is invalid`() {
        val dto = CreateTransactionDto(
            id = UUID.randomUUID().toString(),
            type = TypeTransaction.CREDIT.name,
            amount = BigDecimal("10.00"),
            currency = Currency.BRL.name,
            status = "INVALID",
            timestamp = Instant.now()
        )

        assertThrows(IllegalArgumentException::class.java) {
            dto.toDomain(UUID.randomUUID())
        }
    }
}
