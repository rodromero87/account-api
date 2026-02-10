package com.bank.account.application.command

import com.bank.account.application.dto.TransactionRecord

interface CommandHandler {
    fun handle(command: TransactionRecord)
}