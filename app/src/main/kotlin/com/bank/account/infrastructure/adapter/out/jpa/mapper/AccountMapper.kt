package com.bank.account.infrastructure.adapter.out.jpa.mapper

import com.bank.account.domain.model.Account
import com.bank.account.domain.model.Balance
import com.bank.account.infrastructure.adapter.out.jpa.entity.AccountEntity

fun Account.toEntity() = AccountEntity(
        id = this.id,
        ownerId = this.owner,
        balanceAmount = this.balance.amount,
        balanceCurrency = this.balance.currency,
        createdAt = this.createAt,
        updatedAt = this.updateAt
    )


fun AccountEntity.toDomain() = Account(
        id = this.id,
        owner = this.ownerId,
        balance = Balance(this.balanceAmount, this.balanceCurrency),
        createAt = this.createdAt,
        updateAt = this.updatedAt
    )
