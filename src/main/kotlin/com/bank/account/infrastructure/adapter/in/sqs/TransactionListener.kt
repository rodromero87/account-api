package com.bank.account.infrastructure.adapter.`in`.sqs

import com.bank.account.application.command.CommandHandler
import com.bank.account.application.dto.TransactionRecord
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import kotlin.jvm.java

@Component
class TransactionListener(val objectMapper: ObjectMapper
, val handler: CommandHandler) {

    fun onMessage(message: String) {
        try{
            val transactionRecord = objectMapper.readValue(message, TransactionRecord::class.java)
            handler.handle(transactionRecord)
        }catch (Exception: Exception){
            println("Error processing message: ${Exception.message}")
        }

    }

}