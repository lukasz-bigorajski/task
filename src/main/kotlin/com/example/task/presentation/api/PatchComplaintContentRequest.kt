package com.example.task.presentation.api

data class PatchComplaintContentRequest(
    val reporterId: String,
    val productId: String,
    val content: String,
)
