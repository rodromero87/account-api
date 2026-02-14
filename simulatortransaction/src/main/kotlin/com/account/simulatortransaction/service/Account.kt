package com.account.simulatortransaction.service

import java.time.Instant

data class Account(val id: String,
                   val owner: String,
                   val createdAt: Instant,
                   val balance: Balance) {
}