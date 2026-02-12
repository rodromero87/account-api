package com.bank.account.infrastructure.adapter.out.jpa.repository

import com.bank.account.infrastructure.adapter.out.jpa.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AccountJpaRepository : JpaRepository<AccountEntity, UUID> {
}