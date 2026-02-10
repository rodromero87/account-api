package com.bank.account.domain.model

import com.bank.account.domain.enums.Currency
import java.math.BigDecimal

data class Balance(val amount: BigDecimal, val currency: Currency)
