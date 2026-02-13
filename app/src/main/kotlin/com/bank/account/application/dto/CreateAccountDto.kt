package com.bank.account.application.dto

import java.time.Instant

data class CreateAccountDto(val id: String,
                            val owner: String,
                            val createdAt: Instant,
                            val balance: BalanceDto)
