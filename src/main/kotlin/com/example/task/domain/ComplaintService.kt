package com.example.task.domain

import com.example.task.domain.model.Complaint
import com.example.task.domain.model.CreateComplaintCommand
import com.example.task.domain.model.PatchComplaintContentCommand
import com.example.task.presentation.api.NotFoundException
import com.example.task.utils.LoggerExtension.getLogger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.Clock

class ComplaintService(
    private val complaintRepository: ComplaintRepository,
    private val nationalityFetchingService: NationalityFetchingService,
    private val clock: Clock
) {
    fun createOrUpdateComplaint(command: CreateComplaintCommand): Unit {
        val alreadyExistingComplaint = complaintRepository.get(command.complaintId)
        when {
            alreadyExistingComplaint == null -> createComplaint(command)
            else -> increaseComplaintCounter(alreadyExistingComplaint)
        }
    }

    fun patchComplaintContent(command: PatchComplaintContentCommand): Unit {
        val alreadyExistingComplaint = complaintRepository.get(command.complaintId)
        when {
            alreadyExistingComplaint == null -> {
                logger.warn("Cannot find complaint with id: {}", command.complaintId)
                throw NotFoundException("Cannot find complaint")
            }
            else -> complaintRepository.update(alreadyExistingComplaint.copy(content = command.content))
        }
    }

    fun fetchComplaints(pageable: Pageable): Page<Complaint> =
        complaintRepository.getPage(pageable)

    private fun createComplaint(command: CreateComplaintCommand) {
        val nationalityCode = command.ipAddress?.let { nationalityFetchingService.fetchCountry(it) }
        complaintRepository.create(
            Complaint(
                complaintId = command.complaintId,
                content = command.content,
                createdDateTime = clock.instant(),
                countryCode = nationalityCode,
                creationCounter = 1
            )
        )
    }

    private fun increaseComplaintCounter(complaint: Complaint) {
        logger.debug("Complaint already exists")
        complaintRepository.update(
            complaint.copy(creationCounter = complaint.creationCounter + 1)
        )
    }

    companion object {
        private val logger = this.getLogger()
    }
}
