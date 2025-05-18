package com.example.task.presentation.api

import java.time.Instant

data class Complaint(
    val reporterId: String,
    val productId: String,
    val content: String,
    val createdDateTime: Instant,
)
