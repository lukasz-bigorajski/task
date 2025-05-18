package com.example.task.external.service.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "restclient.ipapi")
data class IpApiConfiguration(
    val uri: String,
    val connectionTimeout: Duration,
    val readTimeout: Duration
)
