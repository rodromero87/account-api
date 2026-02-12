package com.bank.account.application.usecase

import com.bank.account.application.dto.AccountDto

interface SearchAccountUseCase {
        fun search(accountNumber: String): AccountDto
}