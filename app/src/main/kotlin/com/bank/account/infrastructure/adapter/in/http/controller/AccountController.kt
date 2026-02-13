package com.bank.account.infrastructure.adapter.`in`.http.controller

import com.bank.account.application.usecase.SearchAccountUseCase
import com.bank.account.infrastructure.adapter.`in`.http.controller.mapper.toResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/accounts")
@RestController
class AccountController(val searchAccountUseCase: SearchAccountUseCase) {

    @GetMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    fun findByAccountNumber(@PathVariable accountNumber: String) = searchAccountUseCase.search(accountNumber).toResponse()
}