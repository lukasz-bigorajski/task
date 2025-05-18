package com.example.task.domain.model

data class CreateComplaintCommand(
    val complaintId: ComplaintId,
    val content: String,
    val ipAddress: IpAddress?,
)
