package com.bank.account.infrastructure.adapter.out.prometheus

import com.bank.account.domain.enums.MetricsTransaction
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class MetricsPrometheusAdapterTest {

    private lateinit var meterRegistry: SimpleMeterRegistry
    private lateinit var adapter: MetricsPrometheusAdapter

    @BeforeEach
    fun setup() {
        meterRegistry = SimpleMeterRegistry()
        adapter = MetricsPrometheusAdapter(meterRegistry)
    }

    @Test
    fun `count should increment counter`() {
        val metric = MetricsTransaction.TOTAL_RECEIVED

        adapter.count(metric)
        adapter.count(metric)

        val counter = meterRegistry.get(metric.metricName).counter()

        assertEquals(2.0, counter.count())
    }

    @Test
    fun `duration should record time in timer`() {
        val metric = MetricsTransaction.PROCESSING_TIME

        adapter.duration(metric, 100)
        adapter.duration(metric, 200)

        val timer = meterRegistry.get(metric.metricName).timer()

        assertEquals(2L, timer.count())
        assertEquals(300.0, timer.totalTime(TimeUnit.MILLISECONDS))
    }
}
