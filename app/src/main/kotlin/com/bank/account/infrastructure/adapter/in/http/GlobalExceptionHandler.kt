package com.bank.account.infrastructure.adapter.`in`.http

import com.bank.account.domain.exception.AccountNotFoundException
import com.bank.account.infrastructure.adapter.`in`.http.controller.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFound(
        ex: AccountNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        val response = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Account Not Found",
            message = ex.message ?: "Account not found",
            path = request.requestURI
        )

        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }
}