package com.bank.account.infrastructure.adapter.`in`.sqs

import com.bank.account.application.command.CommandHandler
import com.bank.account.application.dto.TransactionRecord
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component

@Component
class TransactionListener(
    private val objectMapper: ObjectMapper,
    private val handler: CommandHandler
) {

    @SqsListener("\${app.sqs.transactions-queue}")
    fun onMessage(message: String) {
        try {
            val transactionRecord =
                objectMapper.readValue(message, TransactionRecord::class.java)

            handler.handle(transactionRecord)

        } catch (e: Exception) {
            println("Error processing message: ${e.message}")
           // throw e
        }
    }
}
