package com.bank.account.infrastructure.adapter.out.jpa

import com.bank.account.domain.enums.Currency
import com.bank.account.domain.enums.StatusTransaction
import com.bank.account.domain.enums.TypeTransaction
import com.bank.account.domain.model.Transaction
import com.bank.account.infrastructure.adapter.out.jpa.entity.TransactionEntity
import com.bank.account.infrastructure.adapter.out.jpa.repository.TransactionJpaRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class TransactionAdapterTest {

    private lateinit var repository: TransactionJpaRepository
    private lateinit var adapter: TransactionAdapter

    @BeforeEach
    fun setup() {
        repository = mockk()
        adapter = TransactionAdapter(repository)
    }

    @Test
    fun `create should save mapped entity`() {
        val tx = transactionDomain()

        val entitySlot = slot<TransactionEntity>()
        every { repository.save(capture(entitySlot)) } answers { entitySlot.captured }

        adapter.create(tx)

        assertEquals(tx.id, entitySlot.captured.id)
        assertEquals(tx.accountId, entitySlot.captured.accountId)
        assertEquals(tx.amount, entitySlot.captured.amount)
        assertEquals(tx.type, entitySlot.captured.type)
        assertEquals(tx.currency, entitySlot.captured.currency)
        assertEquals(tx.status, entitySlot.captured.status)
        assertEquals(tx.timestamp, entitySlot.captured.timestamp)

        verify(exactly = 1) { repository.save(any<TransactionEntity>()) }
        confirmVerified(repository)
    }

    @Test
    fun `create should swallow DataIntegrityViolationException`() {
        val tx = transactionDomain()

        every { repository.save(any<TransactionEntity>()) } throws DataIntegrityViolationException("dup")

        adapter.create(tx)

        verify(exactly = 1) { repository.save(any<TransactionEntity>()) }
        confirmVerified(repository)
    }

    @Test
    fun `findByAccount should map entities to domain`() {
        val accountId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val e1 = TransactionEntity(
            id = UUID.randomUUID(),
            accountId = accountId,
            amount = BigDecimal("10.00"),
            type = TypeTransaction.CREDIT,
            currency = Currency.BRL,
            status = StatusTransaction.APPROVED,
            timestamp = now
        )

        val e2 = TransactionEntity(
            id = UUID.randomUUID(),
            accountId = accountId,
            amount = BigDecimal("20.00"),
            type = TypeTransaction.DEBIT,
            currency = Currency.BRL,
            status = StatusTransaction.APPROVED,
            timestamp = now
        )

        every { repository.findByAccountId(accountId) } returns listOf(e1, e2)

        val result = adapter.findByAccount(accountId)

        assertEquals(
            listOf(
                Transaction(
                    id = e1.id,
                    accountId = accountId,
                    amount = BigDecimal("10.00"),
                    type = TypeTransaction.CREDIT,
                    currency = Currency.BRL,
                    status = StatusTransaction.APPROVED,
                    timestamp = now
                ),
                Transaction(
                    id = e2.id,
                    accountId = accountId,
                    amount = BigDecimal("20.00"),
                    type = TypeTransaction.DEBIT,
                    currency = Currency.BRL,
                    status = StatusTransaction.APPROVED,
                    timestamp = now
                )
            ),
            result
        )

        verify(exactly = 1) { repository.findByAccountId(accountId) }
        confirmVerified(repository)
    }

    @Test
    fun `exist should delegate to repository`() {
        val id = UUID.randomUUID()
        every { repository.existsById(id) } returns true

        val result = adapter.exist(id)

        assertEquals(true, result)
        verify(exactly = 1) { repository.existsById(id) }
        confirmVerified(repository)
    }

    private fun transactionDomain(): Transaction {
        val now = Instant.parse("2026-02-13T00:00:00Z")
        return Transaction(
            id = UUID.randomUUID(),
            accountId = UUID.randomUUID(),
            amount = BigDecimal("10.00"),
            type = TypeTransaction.CREDIT,
            currency = Currency.BRL,
            status = StatusTransaction.APPROVED,
            timestamp = now
        )
    }
}
