package com.bank.account.infrastructure.adapter.out.jpa

import com.bank.account.domain.exception.AccountNotFoundException
import com.bank.account.domain.model.Account
import com.bank.account.domain.out.port.SearchAccountPort
import com.bank.account.domain.out.port.UpdateAccountPort
import com.bank.account.infrastructure.adapter.out.jpa.mapper.toDomain
import com.bank.account.infrastructure.adapter.out.jpa.mapper.toEntity
import com.bank.account.infrastructure.adapter.out.jpa.repository.AccountJpaRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class AccountAdapter(val repository: AccountJpaRepository) : SearchAccountPort, UpdateAccountPort {

    private val log = LoggerFactory.getLogger(AccountAdapter::class.java)

    override fun searchAccount(id: UUID): Account {
        repository.findById(id).orElseThrow { AccountNotFoundException("Account ${id} not found") }.let {
            return it.toDomain()
        }
    }

    override fun updateAccount(account: Account) {
        try {
            repository.save(account.toEntity())
        }catch (e: DataIntegrityViolationException){
            log.error("Data integrity violation for account ${account.id}", e)
            return
        }
    }
}