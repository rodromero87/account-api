package com.bank.account.domain.enums

enum class StatusAccount {
    ENABLED, DISABLED;

    companion object {
        fun from(value: String): StatusAccount =
            StatusAccount.entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid Status Account: $value")
    }
}