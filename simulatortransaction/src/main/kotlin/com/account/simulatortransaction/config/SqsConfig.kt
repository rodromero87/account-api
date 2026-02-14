package com.account.simulatortransaction.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import java.net.URI

@Configuration
class SqsConfig {

    @Bean
    fun sqsAsyncClient(
        @Value("\${simulator.sqs.endpoint}") endpoint: String,
        @Value("\${simulator.sqs.region}") region: String
    ): SqsAsyncClient {

        val creds = AwsBasicCredentials.create("test", "test")

        return SqsAsyncClient.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(creds))
            .build()
    }
}