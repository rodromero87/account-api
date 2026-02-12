package com.bank.account.infrastructure.adapter.out.jpa.mapper

import com.bank.account.domain.model.Transaction
import com.bank.account.infrastructure.adapter.out.jpa.entity.TransactionEntity
import com.bank.account.infrastructure.adapter.out.jpa.helper.epochToInstant

fun Transaction.toEntity() = TransactionEntity(
        id = this.id,
        accountId = this.accountId,
        amount = this.amount,
        currency = this.currency,
        type = this.type,
        status = this.status,
        timestamp = epochToInstant(this.timestamp.epochSecond)
    )

fun TransactionEntity.toDomain() = Transaction(
        id = this.id,
        accountId = this.accountId,
        amount = this.amount,
        currency = this.currency,
        type = this.type,
        status = this.status,
        timestamp = this.timestamp
    )