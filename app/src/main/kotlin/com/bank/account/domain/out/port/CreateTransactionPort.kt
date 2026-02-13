package com.bank.account.domain.out.port

import com.bank.account.domain.model.Transaction

interface CreateTransactionPort {
        fun create(transaction: Transaction)
}