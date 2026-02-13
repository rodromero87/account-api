package com.bank.account.domain.enums

enum class Currency {
    BRL, USD, EUR;

    companion object {
        fun from(value: String): Currency =
            Currency.entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid currency type: $value")
    }
}