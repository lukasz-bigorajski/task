package com.example.task.domain.model

data class PatchComplaintContentCommand(
    val complaintId: ComplaintId,
    val content: String,
)
