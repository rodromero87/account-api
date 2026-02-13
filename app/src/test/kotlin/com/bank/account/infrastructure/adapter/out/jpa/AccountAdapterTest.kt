package com.bank.account.infrastructure.adapter.out.jpa

import com.bank.account.domain.enums.Currency
import com.bank.account.domain.exception.AccountNotFoundException
import com.bank.account.domain.model.Account
import com.bank.account.domain.model.Balance
import com.bank.account.infrastructure.adapter.out.jpa.entity.AccountEntity
import com.bank.account.infrastructure.adapter.out.jpa.repository.AccountJpaRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import java.math.BigDecimal
import java.time.Instant
import java.util.Optional
import java.util.UUID

class AccountAdapterTest {

    private lateinit var repository: AccountJpaRepository
    private lateinit var adapter: AccountAdapter

    @BeforeEach
    fun setup() {
        repository = mockk()
        adapter = AccountAdapter(repository)
    }

    @Test
    fun `searchAccount should return domain mapped from entity`() {
        val id = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val entity = AccountEntity(
            id = id,
            ownerId = ownerId,
            balanceAmount = BigDecimal("100.00"),
            balanceCurrency = Currency.BRL,
            createdAt = now,
            updatedAt = now
        )

        every { repository.findById(id) } returns Optional.of(entity)

        val result = adapter.searchAccount(id)

        assertEquals(
            Account(
                id = id,
                owner = ownerId,
                balance = Balance(BigDecimal("100.00"), Currency.BRL),
                createAt = now,
                updateAt = now
            ),
            result
        )

        verify(exactly = 1) { repository.findById(id) }
        confirmVerified(repository)
    }

    @Test
    fun `searchAccount should throw AccountNotFoundException when not found`() {
        val id = UUID.randomUUID()

        every { repository.findById(id) } returns Optional.empty()

        assertThrows(AccountNotFoundException::class.java) {
            adapter.searchAccount(id)
        }

        verify(exactly = 1) { repository.findById(id) }
        confirmVerified(repository)
    }

    @Test
    fun `updateAccount should save mapped entity`() {
        val id = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val account = Account(
            id = id,
            owner = ownerId,
            balance = Balance(BigDecimal("50.00"), Currency.BRL),
            createAt = now,
            updateAt = now
        )

        val entitySlot = slot<AccountEntity>()

        every { repository.save(capture(entitySlot)) } answers { entitySlot.captured }

        adapter.updateAccount(account)

        assertEquals(id, entitySlot.captured.id)
        assertEquals(ownerId, entitySlot.captured.ownerId)
        assertEquals(BigDecimal("50.00"), entitySlot.captured.balanceAmount)
        assertEquals(Currency.BRL, entitySlot.captured.balanceCurrency)
        assertEquals(now, entitySlot.captured.createdAt)
        assertEquals(now, entitySlot.captured.updatedAt)

        verify(exactly = 1) { repository.save(any<AccountEntity>()) }
        confirmVerified(repository)
    }

    @Test
    fun `updateAccount should swallow DataIntegrityViolationException`() {
        val id = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val account = Account(
            id = id,
            owner = ownerId,
            balance = Balance(BigDecimal("50.00"), Currency.BRL),
            createAt = now,
            updateAt = now
        )

        every { repository.save(any<AccountEntity>()) } throws DataIntegrityViolationException("dup")

        adapter.updateAccount(account)

        verify(exactly = 1) { repository.save(any<AccountEntity>()) }
        confirmVerified(repository)
    }
}
