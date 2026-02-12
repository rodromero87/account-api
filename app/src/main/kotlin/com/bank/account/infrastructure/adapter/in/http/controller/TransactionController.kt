package com.bank.account.infrastructure.adapter.`in`.http.controller

import com.bank.account.application.usecase.SearchTransactionUseCase
import com.bank.account.infrastructure.adapter.`in`.http.controller.mapper.toResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RequestMapping("/transactions")
@RestController
class TransactionController(val searchTransactionUseCase: SearchTransactionUseCase) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun searchByAccount(@RequestParam accountId: String) =
        searchTransactionUseCase.searchByAccount(UUID.fromString(accountId)).toResponse()

}