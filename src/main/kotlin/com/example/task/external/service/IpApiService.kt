package com.example.task.external.service

import com.example.task.domain.NationalityFetchingService
import com.example.task.domain.model.CountryCode
import com.example.task.domain.model.IpAddress
import com.example.task.external.service.model.GetIpApiResponse
import com.example.task.presentation.api.ResourceException
import com.example.task.utils.LoggerExtension.getLogger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

// here also should be configured resilience etc.
// exception handling should also be configured
@Service
class IpApiService(@Qualifier("ipApiRestClient") private val restClient: RestClient) : NationalityFetchingService {
    override fun fetchCountry(ipAddress: IpAddress): CountryCode =
        restClient
            .get()
            .uri { builder ->
                builder
                    .path(ipAddress.value)
                    .build()
            }
            .header("Content-Type", "application/json")
            .retrieve()
            .toEntity(GetIpApiResponse::class.java)
            .body
            ?.let { CountryCode(it.country) }
            ?: run {
                log.warn("Cannot retrieve country code by ip")
                throw ResourceException("Error occurred.")
            }

    companion object {
        private val log = this.getLogger()
    }
}
