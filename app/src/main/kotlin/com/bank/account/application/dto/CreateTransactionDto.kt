package com.bank.account.application.dto

import java.math.BigDecimal
import java.time.Instant

data class CreateTransactionDto(val id: String,
                                val type: String,
                                val amount: BigDecimal,
                                val currency: String,
                                val status: String,
                                val timestamp: Instant) {
}