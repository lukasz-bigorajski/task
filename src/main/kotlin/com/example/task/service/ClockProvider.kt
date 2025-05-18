package com.example.task.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.ZoneId

@Configuration
class ClockProvider {
    @Bean
    fun clock(): Clock = Clock.system(ZONE_ID)

    companion object {
        val ZONE_ID: ZoneId = ZoneId.of("Europe/Warsaw")
    }
}
