package com.bank.account.application.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class TransactionDto(val id: UUID,
                          val accountId: UUID,
                          val type: String,
                          val amount: BigDecimal,
                          val currency: String,
                          val status: String,
                          val creatAt: LocalDateTime) {
}