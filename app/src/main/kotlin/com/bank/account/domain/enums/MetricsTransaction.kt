package com.bank.account.domain.enums

@Suppress("unused")
enum class MetricsTransaction(val metricName: String, val description: String) {
    TOTAL_RECEIVED("account_api_sqs_messages_received_total", "Total messages received from SQS"),
    TOTAL_SUCCESS("account_api_sqs_messages_success_total", "Total messages successfully processed"),
    TOTAL_FAILED("account_api_sqs_messages_failed_total", "Total messages failed processing"),
    PROCESSING_TIME("account_api_processing_time_seconds", "Processing time in seconds")
}