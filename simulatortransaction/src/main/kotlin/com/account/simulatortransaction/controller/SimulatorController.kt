package com.account.simulatortransaction.controller

import com.account.simulatortransaction.controller.dto.SimulationProgressResponse
import com.account.simulatortransaction.controller.dto.SimulationRequest
import com.account.simulatortransaction.controller.dto.SimulationResponse
import com.account.simulatortransaction.service.SimulatorTransactionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/simulator/transactions")
class SimulatorController(val service: SimulatorTransactionService) {

    @PostMapping("/start")
    fun start(@RequestBody @Valid req: SimulationRequest): ResponseEntity<SimulationResponse> {
        return ResponseEntity.ok(service.start(req))
    }

    @GetMapping("/{simulationId}/progress")
    fun progress(@PathVariable simulationId: String): ResponseEntity<SimulationProgressResponse> {
        return ResponseEntity.ok(service.progress(simulationId))
    }
}