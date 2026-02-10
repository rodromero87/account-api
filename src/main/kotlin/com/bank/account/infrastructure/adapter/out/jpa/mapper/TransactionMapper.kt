package com.bank.account.infrastructure.adapter.out.jpa.mapper

import com.bank.account.domain.model.Transaction
import com.bank.account.infrastructure.adapter.out.jpa.entity.TransactionEntity

fun Transaction.toEntity() = TransactionEntity(
        id = this.id,
        accountId = this.accountId,
        amount = this.amount,
        currency = this.currency,
        type = this.type,
        status = this.status,
        timestamp = this.timestamp
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