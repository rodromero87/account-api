package com.account.simulatortransaction.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest
import java.util.concurrent.ConcurrentHashMap

@Component
class SqsQueueResolver(
    private val client: SqsAsyncClient,
    @Value("\${simulator.sqs.queueName}") private val queueName: String,
    @Value("\${simulator.sqs.queueUrl:}") private val queueUrl: String,
    @Value("\${simulator.sqs.useQueueUrl:false}") private val useQueueUrl: Boolean
) {
    private val cache = ConcurrentHashMap<String, String>()

    suspend fun resolveQueueUrl(): String {
        if (useQueueUrl && queueUrl.isNotBlank()) return queueUrl
        return cache.computeIfAbsent(queueName) { _ ->

            client.createQueue(CreateQueueRequest.builder().queueName(queueName).build()).join()
            val resp = client.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).join()
            resp.queueUrl()
        }
    }
}