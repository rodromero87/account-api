package com.bank.account.domain.exception

class AccountNotFoundException(message: String = "Account not found") : RuntimeException(message) {
}