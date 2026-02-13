package com.bank.account.infrastructure.adapter.`in`.http.controller.dto

import com.bank.account.domain.enums.Currency
import com.bank.account.domain.enums.StatusTransaction
import com.bank.account.domain.enums.TypeTransaction
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class TransactionResponse(val id: UUID,
                               val accountId: UUID,
                               val type: String,
                               val amount: BigDecimal,
                               val currency: String,
                               val status: String,
                               val creatAt: LocalDateTime)
