package com.account.simulatortransaction.service

import java.math.BigDecimal
import java.time.Instant

data class Transaction(val id: String,
                       val type: String,
                       val amount: BigDecimal,
                       val currency: String,
                       val status: String,
                       val timestamp: Instant)
