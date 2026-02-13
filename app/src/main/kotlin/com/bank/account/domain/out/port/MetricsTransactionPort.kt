package com.bank.account.domain.out.port

import com.bank.account.domain.enums.MetricsTransaction

interface MetricsTransactionPort {

    fun count(metric: MetricsTransaction)

    fun duration(metric: MetricsTransaction, durationInMillis: Long)
}