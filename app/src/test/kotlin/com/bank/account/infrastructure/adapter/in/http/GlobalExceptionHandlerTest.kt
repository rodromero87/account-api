package com.bank.account.infrastructure.adapter.`in`.http

import com.bank.account.domain.exception.AccountNotFoundException
import com.bank.account.infrastructure.adapter.`in`.http.controller.dto.ErrorResponse
import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class GlobalExceptionHandlerTest {

    private val handler = GlobalExceptionHandler()

    @Test
    fun `should return 404 when AccountNotFoundException is thrown`() {
        val exception = AccountNotFoundException("Account 123 not found")

        val request = mockk<HttpServletRequest>()
        every { request.requestURI } returns "/accounts/123"

        val response = handler.handleAccountNotFound(exception, request)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)

        val body = response.body as ErrorResponse
        assertEquals(404, body.status)
        assertEquals("Account Not Found", body.error)
        assertEquals("Account 123 not found", body.message)
        assertEquals("/accounts/123", body.path)
    }

    @Test
    fun `should use default message when exception message is null`() {
        val exception = AccountNotFoundException()

        val request = mockk<HttpServletRequest>()
        every { request.requestURI } returns "/accounts/999"

        val response = handler.handleAccountNotFound(exception, request)

        val body = response.body as ErrorResponse
        assertEquals("Account not found", body.message)
        assertEquals("/accounts/999", body.path)
    }
}
