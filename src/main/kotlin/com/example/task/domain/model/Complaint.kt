package com.example.task.domain.model

import java.time.Instant

data class Complaint(
    val complaintId: ComplaintId,
    val content: String,
    val createdDateTime: Instant,
    val countryCode: CountryCode?,
    val creationCounter: Int,
)
