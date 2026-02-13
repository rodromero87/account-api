package com.bank.account.infrastructure.adapter.out.prometheus

import com.bank.account.domain.enums.MetricsTransaction
import com.bank.account.domain.out.port.MetricsTransactionPort
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class MetricsPrometheusAdapter(val meterRegistry: MeterRegistry) : MetricsTransactionPort{
    override fun count(metric: MetricsTransaction) {
        Counter.builder(metric.metricName)
            .description(metric.description)
            .register(meterRegistry)
            .increment()
    }

    override fun duration(metric: MetricsTransaction, durationInMillis: Long) {
        Timer.builder(metric.metricName)
            .description(metric.description)
            .publishPercentileHistogram()
            .register(meterRegistry)
            .record(durationInMillis, TimeUnit.MILLISECONDS)
    }
}