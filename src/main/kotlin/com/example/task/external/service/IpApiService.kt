package com.example.task.external.service

import com.example.task.domain.NationalityFetchingService
import com.example.task.domain.model.CountryCode
import com.example.task.domain.model.IpAddress
import org.springframework.stereotype.Service

@Service
class IpApiService : NationalityFetchingService {
    override fun fetchCountry(ipAddress: IpAddress): CountryCode {
        // TODO: add it later
        TODO("Not yet implemented")
    }
}
