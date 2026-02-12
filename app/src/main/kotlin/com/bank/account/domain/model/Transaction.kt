package com.bank.account.domain.model

import com.bank.account.domain.enums.Currency
import com.bank.account.domain.enums.StatusTransaction
import com.bank.account.domain.enums.TypeTransaction
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class Transaction(
    val id: UUID,
    val accountId: UUID,
    val type: TypeTransaction,
    val amount: BigDecimal,
    val currency: Currency,
    val status: StatusTransaction,
    val timestamp: Instant
)
