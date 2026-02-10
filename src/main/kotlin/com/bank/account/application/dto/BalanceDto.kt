package com.bank.account.application.dto

import java.math.BigDecimal

data class BalanceDto(val amount: BigDecimal,
                      val currency: String)
