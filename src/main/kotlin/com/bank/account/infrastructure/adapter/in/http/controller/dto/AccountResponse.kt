package com.bank.account.infrastructure.adapter.`in`.http.controller.dto

import java.time.Instant

data class AccountResponse(val id: String,
                           val owner: String,
                           val createAt: Instant,
                           val updateAt: Instant,
                           val balance: BalanceResponse)
