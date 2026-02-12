package com.bank.account.infrastructure.adapter.`in`.http.controller.dto

import java.math.BigDecimal

data class BalanceResponse(val amount: BigDecimal,
                           val currency: String)
