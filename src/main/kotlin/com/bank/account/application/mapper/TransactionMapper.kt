package com.bank.account.application.mapper

import com.bank.account.application.dto.CreateTransactionDto
import com.bank.account.domain.enums.Currency
import com.bank.account.domain.enums.StatusTransaction
import com.bank.account.domain.enums.TypeTransaction
import com.bank.account.domain.model.Transaction
import java.util.UUID

fun CreateTransactionDto.toDomain(accountId: UUID): Transaction {
    return Transaction(
        id = UUID.fromString(this.id),
        accountId = accountId,
        amount = this.amount,
        type = TypeTransaction.from(this.type),
        currency = Currency.from(this.currency),
        status = StatusTransaction.from(this.status),
        timestamp = this.timestamp
    )
}