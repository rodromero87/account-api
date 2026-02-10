package com.bank.account.infrastructure.adapter.out.jpa

import com.bank.account.domain.model.Transaction
import com.bank.account.domain.out.port.CreateTransactionPort
import com.bank.account.domain.out.port.SearchTransactionPort
import com.bank.account.infrastructure.adapter.out.jpa.mapper.toDomain
import com.bank.account.infrastructure.adapter.out.jpa.mapper.toEntity
import com.bank.account.infrastructure.adapter.out.jpa.repository.TransactionJpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class TransactionAdapter(val repository: TransactionJpaRepository) : CreateTransactionPort, SearchTransactionPort {
    override fun create(transaction: Transaction) {
        repository.save(transaction.toEntity())
    }

    override fun findByAccount(accountId: UUID) = repository.findByAccountId(accountId).map { it.toDomain() }


    override fun exist(id: UUID) = repository.existsById(id)
}