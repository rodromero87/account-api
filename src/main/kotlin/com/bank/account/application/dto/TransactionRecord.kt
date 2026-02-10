package com.bank.account.application.dto

data class TransactionRecord(
    val transaction: CreateTransactionDto,
    val account: CreateAccountDto
)
