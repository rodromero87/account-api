package com.bank.account.application.service

import com.bank.account.application.mapper.toDto
import com.bank.account.application.usecase.SearchAccountUseCase

import com.bank.account.domain.out.port.SearchAccountPort

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SearchAccountService(
    val searchAccountPort: SearchAccountPort) : SearchAccountUseCase {

    override fun search(accountNumber: String)
    = searchAccountPort.searchAccount(UUID.fromString(accountNumber)).toDto()
}