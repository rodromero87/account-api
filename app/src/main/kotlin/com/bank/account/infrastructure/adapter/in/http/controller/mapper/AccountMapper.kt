package com.bank.account.infrastructure.adapter.`in`.http.controller.mapper

import com.bank.account.application.dto.AccountDto
import com.bank.account.application.dto.BalanceDto
import com.bank.account.infrastructure.adapter.`in`.http.controller.dto.AccountResponse
import com.bank.account.infrastructure.adapter.`in`.http.controller.dto.BalanceResponse

fun AccountDto.toResponse() = AccountResponse(
    id = this.id,
    owner = this.owner,
    createAt = this.createAt,
    updateAt = this.updateAt,
    balance = this.balance.toResponse()
)

fun BalanceDto.toResponse() = BalanceResponse(
    amount = this.amount,
    currency = this.currency
)