package com.example.task.domain

import com.example.task.domain.model.Complaint
import com.example.task.domain.model.ComplaintId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ComplaintRepository {

    fun create(complaint: Complaint): Complaint
    fun update(complaint: Complaint): Complaint
    fun get(complaintId: ComplaintId): Complaint?
    fun getPage(pageable: Pageable): Page<Complaint>
}
