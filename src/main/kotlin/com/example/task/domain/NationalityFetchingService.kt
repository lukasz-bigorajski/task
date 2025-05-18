package com.example.task.domain

import com.example.task.domain.model.CountryCode
import com.example.task.domain.model.IpAddress

interface NationalityFetchingService {
    fun fetchCountry(ipAddress: IpAddress): CountryCode
}
