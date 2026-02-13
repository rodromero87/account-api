package com.bank.account.domain.enums


enum class TypeTransaction {
    CREDIT, DEBIT;

    companion object {
        fun from(value: String): TypeTransaction =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid transaction type: $value")
    }
}