package com.bank.account.domain.enums

enum class StatusTransaction {
    APPROVED, REJECTED, PENDING;

    companion object {
        fun from(value: String): StatusTransaction =
            StatusTransaction.entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid status transaction: $value")
    }
}