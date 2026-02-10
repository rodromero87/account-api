package com.bank.account.application.usecase

import java.util.UUID

interface SearchTransactionUseCase {
    fun existsTransaction(id: UUID): Boolean
}