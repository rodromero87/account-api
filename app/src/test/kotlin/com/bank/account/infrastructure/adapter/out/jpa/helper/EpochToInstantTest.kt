package com.bank.account.infrastructure.adapter.out.jpa.helper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

class EpochToInstantTest {

    @Test
    fun `should convert microseconds epoch to Instant`() {
        val epochMicros = 1_700_000_000_123_456L

        val expectedSeconds = epochMicros / 1_000_000
        val expectedNanos = (epochMicros % 1_000_000) * 1_000
        val expected = Instant.ofEpochSecond(expectedSeconds, expectedNanos)

        val result = epochToInstant(epochMicros)

        assertEquals(expected, result)
    }

    @Test
    fun `should convert milliseconds epoch to Instant`() {
        val epochMillis = 1_700_000_000_123L
        val expected = Instant.ofEpochMilli(epochMillis)

        val result = epochToInstant(epochMillis)

        assertEquals(expected, result)
    }

    @Test
    fun `should convert seconds epoch to Instant`() {
        val epochSeconds = 1_700_000_000L
        val expected = Instant.ofEpochSecond(epochSeconds)

        val result = epochToInstant(epochSeconds)

        assertEquals(expected, result)
    }
}
