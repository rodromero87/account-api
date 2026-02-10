package com.bank.account.application.service

import com.bank.account.application.dto.CreateTransactionDto
import com.bank.account.application.mapper.toDomain
import com.bank.account.application.usecase.CreateTransactionUseCase
import com.bank.account.application.usecase.SearchTransactionUseCase
import com.bank.account.domain.out.port.CreateTransactionPort
import com.bank.account.domain.out.port.SearchTransactionPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionService(val createTransactionPort: CreateTransactionPort,
    val searchTransactionPort: SearchTransactionPort) : SearchTransactionUseCase, CreateTransactionUseCase {
    override fun existsTransaction(id: UUID): Boolean {
        return searchTransactionPort.exist(id)
    }

    override fun create(transaction: CreateTransactionDto, accountId: String) {
        createTransactionPort.create(
            transaction.toDomain(accountId = UUID.fromString(accountId))
        )
    }
}