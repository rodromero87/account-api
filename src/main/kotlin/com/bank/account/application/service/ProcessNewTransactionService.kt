package com.bank.account.application.service

import com.bank.account.application.dto.CreateAccountDto
import com.bank.account.application.dto.CreateTransactionDto
import com.bank.account.application.mapper.toDomain
import com.bank.account.application.usecase.ProcessNewTransactionUseCase
import com.bank.account.domain.out.port.CreateTransactionPort
import com.bank.account.domain.out.port.UpdateAccountPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProcessNewTransactionService(val createTransactionPort: CreateTransactionPort,
    val updateAccountPort: UpdateAccountPort) : ProcessNewTransactionUseCase {
    override fun process(
        transaction: CreateTransactionDto,
        accountDto: CreateAccountDto
    ) {
        createTransactionPort.create(transaction.toDomain(UUID.fromString(accountDto.id)))
        updateAccountPort.updateAccount(accountDto.toDomain())
    }
}