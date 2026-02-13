package com.bank.account.infrastructure.adapter.`in`.http.controller

import com.bank.account.application.dto.AccountDto
import com.bank.account.application.dto.BalanceDto
import com.bank.account.application.usecase.SearchAccountUseCase
import com.bank.account.domain.exception.AccountNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class AccountControllerTest {

    private lateinit var searchAccountUseCase: SearchAccountUseCase
    private lateinit var controller: AccountController

    @BeforeEach
    fun setup() {
        searchAccountUseCase = mockk()
        controller = AccountController(searchAccountUseCase)
    }

    @Test
    fun `should return AccountResponse when account exists`() {
        val id = UUID.randomUUID().toString()
        val now = Instant.parse("2026-02-13T00:00:00Z")

        val dto = AccountDto(
            id = id,
            owner = UUID.randomUUID().toString(),
            createAt = now,
            updateAt = now,
            balance = BalanceDto(
                amount = BigDecimal("100.00"),
                currency = "BRL"
            )
        )

        every { searchAccountUseCase.search(id) } returns dto

        val response = controller.findByAccountNumber(id)

        assertEquals(id, response.id)
        assertEquals(dto.owner, response.owner)
        assertEquals(BigDecimal("100.00"), response.balance.amount)
        assertEquals("BRL", response.balance.currency)

        verify(exactly = 1) { searchAccountUseCase.search(id) }
    }

    @Test
    fun `should propagate exception from use case`() {
        val id = UUID.randomUUID().toString()

        every { searchAccountUseCase.search(id) } throws AccountNotFoundException("not found")

        assertThrows(AccountNotFoundException::class.java) {
            controller.findByAccountNumber(id)
        }

        verify(exactly = 1) { searchAccountUseCase.search(id) }
    }
}
