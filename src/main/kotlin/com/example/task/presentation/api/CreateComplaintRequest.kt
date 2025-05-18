package com.example.task.presentation.api

import jakarta.validation.constraints.NotBlank

data class CreateComplaintRequest(
    val reporterId: String,
    val productId: String,
    @field:NotBlank
    val content: String,
)
