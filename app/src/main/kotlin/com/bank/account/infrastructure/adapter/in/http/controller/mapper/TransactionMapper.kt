package com.bank.account.infrastructure.adapter.`in`.http.controller.mapper

import com.bank.account.application.dto.SearchTransactionDto
import com.bank.account.application.dto.TransactionDto
import com.bank.account.infrastructure.adapter.`in`.http.controller.dto.SearchTransactionResponse
import com.bank.account.infrastructure.adapter.`in`.http.controller.dto.TransactionResponse

fun SearchTransactionDto.toResponse() = SearchTransactionResponse(transactions.map { it.toResponse() })

fun TransactionDto.toResponse() = TransactionResponse(
    id = this.id,
    accountId = this.accountId,
    type = this.type,
    amount = this.amount,
    currency = this.currency,
    status = this.status,
    creatAt = this.creatAt
)