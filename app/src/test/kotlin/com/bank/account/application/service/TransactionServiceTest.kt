package com.bank.account.application.service

import com.bank.account.application.dto.SearchTransactionDto
import com.bank.account.application.mapper.toDto
import com.bank.account.domain.model.Transaction
import com.bank.account.domain.out.port.SearchTransactionPort
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class TransactionServiceTest {

    private lateinit var searchTransactionPort: SearchTransactionPort
    private lateinit var service: TransactionService

    @BeforeEach
    fun setup() {
        searchTransactionPort = mockk()
        service = TransactionService(searchTransactionPort)
    }

    @Test
    fun `existsTransaction should return true when port returns true`() {
        val id = UUID.randomUUID()
        every { searchTransactionPort.exist(id) } returns true

        val result = service.existsTransaction(id)

        assertEquals(true, result)
        verify(exactly = 1) { searchTransactionPort.exist(id) }
        confirmVerified(searchTransactionPort)
    }

    @Test
    fun `existsTransaction should return false when port returns false`() {
        val id = UUID.randomUUID()
        every { searchTransactionPort.exist(id) } returns false

        val result = service.existsTransaction(id)

        assertEquals(false, result)
        verify(exactly = 1) { searchTransactionPort.exist(id) }
        confirmVerified(searchTransactionPort)
    }

    @Test
    fun `searchByAccount should map domain transactions to dto`() {
        val accountId = UUID.randomUUID()

        val tx1 = mockk<Transaction>(relaxed = true)
        val tx2 = mockk<Transaction>(relaxed = true)

        every { searchTransactionPort.findByAccount(accountId) } returns listOf(tx1, tx2)

        val result = service.searchByAccount(accountId)

        verify(exactly = 1) { searchTransactionPort.findByAccount(accountId) }

        val expected = SearchTransactionDto(listOf(tx1.toDto(), tx2.toDto()))
        assertEquals(expected, result)

        confirmVerified(searchTransactionPort)
    }

    @Test
    fun `searchByAccount should return empty list when port returns empty`() {
        val accountId = UUID.randomUUID()

        every { searchTransactionPort.findByAccount(accountId) } returns emptyList()

        val result = service.searchByAccount(accountId)

        verify(exactly = 1) { searchTransactionPort.findByAccount(accountId) }
        assertEquals(SearchTransactionDto(emptyList()), result)

        confirmVerified(searchTransactionPort)
    }

    @Test
    fun `existsTransaction should propagate exception when port throws`() {
        val id = UUID.randomUUID()
        every { searchTransactionPort.exist(id) } throws RuntimeException("db error")

        assertThrows(RuntimeException::class.java) {
            service.existsTransaction(id)
        }

        verify(exactly = 1) { searchTransactionPort.exist(id) }
        confirmVerified(searchTransactionPort)
    }

    @Test
    fun `searchByAccount should propagate exception when port throws`() {
        val accountId = UUID.randomUUID()
        every { searchTransactionPort.findByAccount(accountId) } throws RuntimeException("db error")

        assertThrows(RuntimeException::class.java) {
            service.searchByAccount(accountId)
        }

        verify(exactly = 1) { searchTransactionPort.findByAccount(accountId) }
        confirmVerified(searchTransactionPort)
    }
}
