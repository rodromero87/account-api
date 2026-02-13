package com.bank.account.application.service

import com.bank.account.application.dto.*
import com.bank.account.domain.enums.StatusTransaction
import com.bank.account.domain.model.Transaction
import com.bank.account.domain.model.Account
import com.bank.account.domain.out.port.CreateTransactionPort
import com.bank.account.domain.out.port.UpdateAccountPort
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class ProcessNewTransactionServiceTest {

    private lateinit var createTransactionPort: CreateTransactionPort
    private lateinit var updateAccountPort: UpdateAccountPort
    private lateinit var service: ProcessNewTransactionService

    @BeforeEach
    fun setup() {
        createTransactionPort = mockk(relaxed = true)
        updateAccountPort = mockk(relaxed = true)
        service = ProcessNewTransactionService(createTransactionPort, updateAccountPort)
    }

    @Test
    fun `should create transaction with accountId from accountDto and update account`() {
        val accountId = UUID.randomUUID()

        val transactionDto = createTransactionDto(UUID.randomUUID().toString())
        val accountDto = createAccountDto(accountId.toString())

        val txSlot = slot<Transaction>()
        val accSlot = slot<Account>()

        every { createTransactionPort.create(capture(txSlot)) } just Runs
        every { updateAccountPort.updateAccount(capture(accSlot)) } just Runs

        service.process(transactionDto, accountDto)

        verify(exactly = 1) { createTransactionPort.create(any<Transaction>()) }
        verify(exactly = 1) { updateAccountPort.updateAccount(any<Account>()) }

        assert(txSlot.captured.accountId == accountId)

        confirmVerified(createTransactionPort, updateAccountPort)
    }

    @Test
    fun `should throw when accountDto id is not a valid UUID and not call ports`() {
        val transactionDto = createTransactionDto(UUID.randomUUID().toString())
        val accountDto = createAccountDto("not-a-uuid")

        assertThrows(IllegalArgumentException::class.java) {
            service.process(transactionDto, accountDto)
        }

        verify(exactly = 0) { createTransactionPort.create(any<Transaction>()) }
        verify(exactly = 0) { updateAccountPort.updateAccount(any<Account>()) }

        confirmVerified(createTransactionPort, updateAccountPort)
    }


    private fun createTransactionDto(id: String): CreateTransactionDto {
        val now = Instant.parse("2026-02-13T00:00:00Z")
        return CreateTransactionDto(
            id = id,
            type = "CREDIT",
            amount = BigDecimal("10.00"),
            currency = "BRL",
            status = StatusTransaction.APPROVED.name,
            timestamp = now
        )
    }

    private fun createAccountDto(id: String): CreateAccountDto {
        val now = Instant.parse("2026-02-13T00:00:00Z")
        return CreateAccountDto(
            id = id,
            owner = UUID.randomUUID().toString(),
            createdAt = now,
            balance = BalanceDto(
                amount = BigDecimal("0.00"),
                currency = "BRL"
            )
        )
    }

}
