package com.bank.account.infrastructure.adapter.out.jpa

import com.bank.account.domain.exception.AccountNotFoundException
import com.bank.account.domain.model.Account
import com.bank.account.domain.out.port.SearchAccountPort
import com.bank.account.domain.out.port.UpdateAccountPort
import com.bank.account.infrastructure.adapter.out.jpa.mapper.toDomain
import com.bank.account.infrastructure.adapter.out.jpa.mapper.toEntity
import com.bank.account.infrastructure.adapter.out.jpa.repository.AccountJpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class AccountAdapter(val repository: AccountJpaRepository) : SearchAccountPort, UpdateAccountPort {
    override fun searchAccount(id: UUID): Account {
        repository.findById(id).orElseThrow { AccountNotFoundException("Account ${id} not found") }.let {
            return it.toDomain()
        }
    }

    override fun updateAccount(account: Account) {
        repository.save(account.toEntity())
    }
}