package com.example.task.external.service.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.time.Duration

@Configuration
class RestClientsConfiguration(private val ipApiConfiguration: IpApiConfiguration) {

    // Here also should be configured logging, metrics etc.
    @Bean("ipApiRestClient")
    fun ipApiRestClient(): RestClient = RestClient
        .builder()
        .baseUrl(ipApiConfiguration.uri)
        .requestFactory(requestFactory(ipApiConfiguration.connectionTimeout, ipApiConfiguration.readTimeout))
        .build()

    private fun requestFactory(connectTimeout: Duration, readTimeout: Duration): ClientHttpRequestFactory {
        val factory = SimpleClientHttpRequestFactory()
        factory.setReadTimeout(readTimeout)
        factory.setConnectTimeout(connectTimeout)
        return factory
    }
}
