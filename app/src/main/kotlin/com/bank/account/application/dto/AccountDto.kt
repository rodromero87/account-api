package com.bank.account.application.dto

import java.time.Instant

data class AccountDto(val id: String,
                      val owner: String,
                      val createAt: Instant,
                      val updateAt: Instant,
                      val balance: BalanceDto)
