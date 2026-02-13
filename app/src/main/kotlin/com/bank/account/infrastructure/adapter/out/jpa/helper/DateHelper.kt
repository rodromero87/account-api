package com.bank.account.infrastructure.adapter.out.jpa.helper

import java.time.Instant

fun epochToInstant(epoch: Long): Instant {
    return when {
        epoch >= 1_000_000_000_000_000L -> {
            val seconds = epoch / 1_000_000
            val nanos = (epoch % 1_000_000) * 1_000
            Instant.ofEpochSecond(seconds, nanos)
        }
        epoch >= 1_000_000_000_000L -> {
            Instant.ofEpochMilli(epoch)
        }
        else -> {
            Instant.ofEpochSecond(epoch)
        }
    }
}

