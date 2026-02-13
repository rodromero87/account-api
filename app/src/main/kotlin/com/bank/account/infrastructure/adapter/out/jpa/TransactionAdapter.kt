package com.bank.account.infrastructure.adapter.out.jpa

import com.bank.account.domain.model.Transaction
import com.bank.account.domain.out.port.CreateTransactionPort
import com.bank.account.domain.out.port.SearchTransactionPort
import com.bank.account.infrastructure.adapter.out.jpa.mapper.toDomain
import com.bank.account.infrastructure.adapter.out.jpa.mapper.toEntity
import com.bank.account.infrastructure.adapter.out.jpa.repository.TransactionJpaRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class TransactionAdapter(val repository: TransactionJpaRepository) : CreateTransactionPort, SearchTransactionPort {

    private val log = LoggerFactory.getLogger(TransactionAdapter::class.java)

    override fun create(transaction: Transaction) {
        try {
            repository.save(transaction.toEntity())
        } catch (e: DataIntegrityViolationException) {
            log.error("Data integrity violation for transaction ${transaction.id}", e)
            return
        }
    }

    override fun findByAccount(accountId: UUID) = repository.findByAccountId(accountId).map { it.toDomain() }


    override fun exist(id: UUID) = repository.existsById(id)
}