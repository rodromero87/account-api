package com.bank.account.application.usecase

import com.bank.account.application.dto.CreateAccountDto

interface UpdateAccountUseCase {
    fun update(account: CreateAccountDto)
}