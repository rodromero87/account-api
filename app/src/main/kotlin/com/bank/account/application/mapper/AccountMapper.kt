package com.bank.account.application.mapper

import com.bank.account.application.dto.AccountDto
import com.bank.account.application.dto.CreateAccountDto
import com.bank.account.application.dto.BalanceDto
import com.bank.account.domain.enums.Currency
import com.bank.account.domain.model.Account
import com.bank.account.domain.model.Balance
import java.util.UUID

fun CreateAccountDto.toDomain() = Account(
    id = UUID.fromString(this.id),
    owner = UUID.fromString(this.owner),
    createAt = this.createdAt,
    balance = this.balance.toDomain()
)

fun BalanceDto.toDomain() = Balance(
    currency = Currency.from(this.currency),
    amount = this.amount
)

fun Account.toDto() = AccountDto(
    id = this.id.toString(),
    owner = this.owner.toString(),
    createAt = this.createAt,
    updateAt = this.updateAt,
    balance = this.balance.toDto()
)

fun Balance.toDto() = BalanceDto(
    currency = this.currency.name,
    amount = this.amount
)