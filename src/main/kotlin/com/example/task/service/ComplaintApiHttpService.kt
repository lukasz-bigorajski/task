package com.example.task.service

import com.example.task.domain.ComplaintRepository
import com.example.task.domain.ComplaintService
import com.example.task.domain.NationalityFetchingService
import com.example.task.domain.model.ComplaintId
import com.example.task.domain.model.ComplaintReporterId
import com.example.task.domain.model.CreateComplaintCommand
import com.example.task.domain.model.IpAddress
import com.example.task.domain.model.PatchComplaintContentCommand
import com.example.task.domain.model.ProductId
import com.example.task.presentation.api.Complaint
import com.example.task.presentation.api.CreateComplaintRequest
import com.example.task.presentation.api.PageData
import com.example.task.presentation.api.PagedResponse
import com.example.task.presentation.api.PatchComplaintContentRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class ComplaintApiHttpService(
    complaintRepository: ComplaintRepository,
    nationalityFetchingService: NationalityFetchingService,
    clock: Clock,
) {
    private val complaintService: ComplaintService = ComplaintService(
        complaintRepository,
        nationalityFetchingService,
        clock
    )

    fun createComplaint(request: CreateComplaintRequest, ip: String?): Unit {
        complaintService.createOrUpdateComplaint(
            CreateComplaintCommand(
                complaintId = ComplaintId(
                    reporterId = ComplaintReporterId(request.reporterId),
                    productId = ProductId(request.productId)
                ),
                content = request.content,
                ipAddress = ip?.let { IpAddress(it) }
            )
        )
    }

    fun patchComplaintContent(request: PatchComplaintContentRequest): Unit {
        complaintService.patchComplaintContent(
            PatchComplaintContentCommand(
                complaintId = ComplaintId(
                    reporterId = ComplaintReporterId(request.reporterId),
                    productId = ProductId(request.productId)
                ),
                content = request.content
            )
        )
    }

    fun fetchComplaints(pageable: Pageable): PagedResponse<Complaint> = complaintService
        .fetchComplaints(pageable)
        .let { p ->
            PagedResponse(
                elements = p.content.map {
                    Complaint(
                        reporterId = it.complaintId.reporterId.value,
                        productId = it.complaintId.productId.value,
                        content = it.content,
                        createdDateTime = it.createdDateTime
                    )
                },
                pageData = PageData(
                    page = p.number,
                    pageSize = p.size,
                    totalItems = p.totalElements,
                    totalPages = p.totalPages
                )
            )
        }
}
