package com.bank.account.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class JacksonConfigTest {

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        objectMapper = JacksonConfig().objectMapper()
    }

    @Test
    fun `should serialize fields using snake_case`() {
        val dto = TestDto(firstName = "Rodrigo")

        val json = objectMapper.writeValueAsString(dto)

        assertTrue(json.contains("first_name"))
        assertFalse(json.contains("firstName"))
    }

    @Test
    fun `should serialize Instant as ISO-8601 string`() {
        val instant = Instant.parse("2026-02-13T00:00:00Z")
        val dto = TimeDto(createdAt = instant)

        val json = objectMapper.writeValueAsString(dto)

        assertTrue(json.contains("2026-02-13T00:00:00Z"))
        assertFalse(json.contains(instant.epochSecond.toString()))
    }

    @Test
    fun `should ignore unknown properties during deserialization`() {
        val json = """
            {
              "first_name": "Rodrigo",
              "unknown_field": "ignored"
            }
        """.trimIndent()

        val result = objectMapper.readValue(json, TestDto::class.java)

        assertEquals("Rodrigo", result.firstName)
    }

    data class TestDto(val firstName: String)
    data class TimeDto(val createdAt: Instant)
}
