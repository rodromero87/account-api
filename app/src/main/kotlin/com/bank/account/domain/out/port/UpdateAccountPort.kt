package com.bank.account.domain.out.port

import com.bank.account.domain.model.Account

interface UpdateAccountPort {
    fun updateAccount(account: Account)
}