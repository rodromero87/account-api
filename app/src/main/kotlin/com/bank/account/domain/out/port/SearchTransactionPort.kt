package com.bank.account.domain.out.port

import com.bank.account.domain.model.Transaction
import java.util.UUID

interface SearchTransactionPort {
    fun findByAccount(accountId: UUID) : List<Transaction>

    fun exist(id: UUID) : Boolean
}