package com.account.simulatortransaction.controller.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank


data class SimulationRequest(
    @field:Min(1)
    @field:Max(5_000_000)
    val totalMessages: Int,

    // opcional (default 10k/min)
    @field:Min(1)
    @field:Max(200_000)
    val ratePerMinute: Int = 10_000,

    // opcional
    @field:Min(1)
    @field:Max(64)
    val parallelism: Int = 12
)


data class SimulationResponse(
    val simulationId: String,
    val totalMessages: Int,
    val ratePerMinute: Int,
    val parallelism: Int,
    val startedAtEpochMs: Long
)

data class SimulationProgressResponse(
    val simulationId: String,
    val status: String,
    val sent: Long,
    val failed: Long,
    val total: Int,
    val startedAtEpochMs: Long,
    val lastError: String?
)
