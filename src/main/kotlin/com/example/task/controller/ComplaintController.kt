package com.example.task.controller

import com.example.task.presentation.api.Complaint
import com.example.task.presentation.api.CreateComplaintRequest
import com.example.task.presentation.api.PagedResponse
import com.example.task.presentation.api.PatchComplaintContentRequest
import com.example.task.service.ComplaintApiHttpService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/complaint")
class ComplaintController(private val complaintApiHttpService: ComplaintApiHttpService) {

    // Retrieving origin IP depends on my factor, e.g. domain etc. For simplicity, I use 'X-Forwarded-For'
    // and make it optional. For example having domain on cloudflare I could follow:
    // https://developers.cloudflare.com/support/troubleshooting/restoring-visitor-ips/restoring-original-visitor-ips/
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody @Valid request: CreateComplaintRequest,
        @RequestHeader("X-Forwarded-For", required = false) forwardForHeader: String?,
    ) {
        complaintApiHttpService.createComplaint(request, forwardForHeader)
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchContent(@RequestBody request: PatchComplaintContentRequest) {
        complaintApiHttpService.patchComplaintContent(request)
    }

    @GetMapping
    fun listComplaints(pageable: Pageable): PagedResponse<Complaint> = complaintApiHttpService
        .fetchComplaints(pageable)
}
