package com.bank.account.application.service


import com.bank.account.domain.model.Account
import com.bank.account.domain.out.port.SearchAccountPort
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.Instant
import java.util.UUID

class SearchAccountServiceTest {

    private lateinit var searchAccountPort: SearchAccountPort
    private lateinit var service: SearchAccountService

    @BeforeEach
    fun setup() {
        searchAccountPort = mockk()
        service = SearchAccountService(searchAccountPort)
    }

    @Test
    fun `should return account dto when account exists`() {
        val accountId = UUID.randomUUID()

        val domainAccount = Account(
            id = accountId,
            owner = UUID.randomUUID(),
            createAt = Instant.now(),
            balance =
                mockk(relaxed = true)
        )

        every { searchAccountPort.searchAccount(accountId) } returns domainAccount

        val result = service.search(accountId.toString())

        verify(exactly = 1) { searchAccountPort.searchAccount(accountId) }

        assertEquals(domainAccount.id.toString(), result.id)

        confirmVerified(searchAccountPort)
    }

    @Test
    fun `should throw when accountNumber is not a valid UUID`() {
        assertThrows(IllegalArgumentException::class.java) {
            service.search("invalid-uuid")
        }

        verify(exactly = 0) { searchAccountPort.searchAccount(any()) }
    }

    @Test
    fun `should propagate exception when port throws`() {
        val accountId = UUID.randomUUID()

        every { searchAccountPort.searchAccount(accountId) } throws RuntimeException("db error")

        assertThrows(RuntimeException::class.java) {
            service.search(accountId.toString())
        }

        verify(exactly = 1) { searchAccountPort.searchAccount(accountId) }
    }
}
