package com.bank.account.application.usecase

import com.bank.account.application.dto.CreateTransactionDto

interface CreateTransactionUseCase {
    fun create(transaction: CreateTransactionDto)
}