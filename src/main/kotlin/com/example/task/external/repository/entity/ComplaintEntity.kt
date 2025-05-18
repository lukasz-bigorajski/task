package com.example.task.external.repository.entity

import com.example.task.domain.model.Complaint
import com.example.task.domain.model.ComplaintId
import com.example.task.domain.model.ComplaintReporterId
import com.example.task.domain.model.CountryCode
import com.example.task.domain.model.ProductId
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serializable
import java.time.Instant

@Entity
@Table(name = "complaint")
class ComplaintEntity(
    @EmbeddedId
    val id: ComplaintEntityId,

    @Column(name = "content", nullable = false)
    val content: String,

    @Column(name = "created_date_time", nullable = false)
    val createdDateTime: Instant,

    @Column(name = "country_code")
    val countryCode: String?,

    @Column(name = "creation_counter", nullable = false)
    val creationCounter: Int,
) {
    fun toDomain(): Complaint {
        return Complaint(
            complaintId = id.toDomain(),
            content = content,
            createdDateTime = createdDateTime,
            countryCode = countryCode?.let { CountryCode(it) },
            creationCounter = creationCounter
        )
    }

    companion object {
        fun from(complaint: Complaint): ComplaintEntity =
            ComplaintEntity(
                id = ComplaintEntityId.from(complaint.complaintId),
                content = complaint.content,
                createdDateTime = complaint.createdDateTime,
                countryCode = complaint.countryCode?.value,
                creationCounter = complaint.creationCounter
            )
    }
}

@Embeddable
class ComplaintEntityId(
    @Column(name = "reporter_id", nullable = false)
    val reporterId: String,

    @Column(name = "product_id", nullable = false)
    val productId: String
) : Serializable {
    constructor() : this("", "")

    fun toDomain(): ComplaintId {
        return ComplaintId(
            reporterId = ComplaintReporterId(reporterId),
            productId = ProductId(productId)
        )
    }

    companion object {
        fun from(complaintId: ComplaintId): ComplaintEntityId {
            return ComplaintEntityId(
                reporterId = complaintId.reporterId.value,
                productId = complaintId.productId.value
            )
        }
    }
}
