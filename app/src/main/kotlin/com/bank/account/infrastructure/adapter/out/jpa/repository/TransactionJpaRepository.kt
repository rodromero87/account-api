package com.bank.account.infrastructure.adapter.out.jpa.repository

import com.bank.account.infrastructure.adapter.out.jpa.entity.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TransactionJpaRepository : JpaRepository<TransactionEntity, UUID> {
    fun findByAccountId(accountId: UUID) : List<TransactionEntity>
}