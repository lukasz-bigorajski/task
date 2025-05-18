package com.example.task.presentation.api

data class PagedResponse<T>(
    val elements: List<T>,
    val pageData: PageData
)

data class PageData(
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int,
)
