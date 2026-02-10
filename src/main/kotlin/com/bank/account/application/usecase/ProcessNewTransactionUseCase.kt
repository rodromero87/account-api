package com.bank.account.application.usecase

import com.bank.account.application.dto.CreateAccountDto
import com.bank.account.application.dto.CreateTransactionDto

interface ProcessNewTransactionUseCase {

    fun process(transaction: CreateTransactionDto, accountDto: CreateAccountDto)

}