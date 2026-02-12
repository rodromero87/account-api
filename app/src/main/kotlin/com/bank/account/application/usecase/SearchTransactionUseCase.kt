package com.bank.account.application.usecase

import com.bank.account.application.dto.SearchTransactionDto
import java.util.UUID

interface SearchTransactionUseCase {
    fun existsTransaction(id: UUID): Boolean

    fun searchByAccount(accountId: UUID): SearchTransactionDto
}