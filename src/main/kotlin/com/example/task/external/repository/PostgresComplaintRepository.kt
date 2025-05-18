package com.example.task.external.repository

import com.example.task.domain.ComplaintRepository
import com.example.task.domain.model.Complaint
import com.example.task.domain.model.ComplaintId
import com.example.task.external.repository.entity.ComplaintEntity
import com.example.task.external.repository.entity.ComplaintEntityId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
internal class PostgresComplaintRepository(
    private val repository: PostgresComplaintJpaRepository
) : ComplaintRepository {

    override fun create(complaint: Complaint): Complaint = repository
        .save(ComplaintEntity.from(complaint))
        .toDomain()

    override fun update(complaint: Complaint): Complaint = repository
        .save(ComplaintEntity.from(complaint))
        .toDomain()

    override fun get(complaintId: ComplaintId): Complaint? = repository
        .findById(ComplaintEntityId.from(complaintId))
        .getOrNull()
        ?.toDomain()

    override fun getPage(pageable: Pageable): Page<Complaint> = repository
        .findAll(pageable)
        .map { it.toDomain() }
}

@Repository
internal interface PostgresComplaintJpaRepository :
    CrudRepository<ComplaintEntity, ComplaintEntityId>,
    PagingAndSortingRepository<ComplaintEntity, ComplaintEntityId>
