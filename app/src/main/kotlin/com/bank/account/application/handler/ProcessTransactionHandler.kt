package com.bank.account.application.handler

import com.bank.account.application.command.CommandHandler
import com.bank.account.application.dto.TransactionRecord

import com.bank.account.application.usecase.ProcessNewTransactionUseCase
import com.bank.account.application.usecase.SearchTransactionUseCase

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProcessTransactionHandler(val searchTransactionUseCase: SearchTransactionUseCase,
                               val newTransactionUseCase: ProcessNewTransactionUseCase
) : CommandHandler {
    override fun handle(command: TransactionRecord) {

        if(!searchTransactionUseCase.existsTransaction(UUID.fromString(command.transaction.id))) {
           newTransactionUseCase.process(command.transaction, command.account)
        }

    }
}