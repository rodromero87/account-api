package com.bank.account.application.service

import com.bank.account.application.dto.SearchTransactionDto
import com.bank.account.application.mapper.toDto
import com.bank.account.application.usecase.SearchTransactionUseCase
import com.bank.account.domain.out.port.SearchTransactionPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionService(val searchTransactionPort: SearchTransactionPort) : SearchTransactionUseCase {
    override fun existsTransaction(id: UUID): Boolean {
        return searchTransactionPort.exist(id)
    }

    override fun searchByAccount(accountId: UUID) =
        SearchTransactionDto(searchTransactionPort.findByAccount(accountId).map { it.toDto() })


}