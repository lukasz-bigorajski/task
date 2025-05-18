package com.example.task.controller

import com.example.task.BaseSpringUnitTest
import com.example.task.ComplaintDbAsserts
import com.example.task.presentation.api.Complaint
import com.example.task.presentation.api.ExceptionResponse
import com.example.task.presentation.api.PageData
import com.example.task.presentation.api.PagedResponse
import com.example.task.service.ClockProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpHeaders
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Clock
import java.time.Instant
import java.util.UUID

class ComplaintControllerTest : BaseSpringUnitTest() {
    private val baseUrl = "/api/complaint"
    private val mockedDateTime = Instant.now()
    private val complaintDbAsserts by lazy { ComplaintDbAsserts(dataSource) }

    @MockitoBean
    private lateinit var clock: Clock

    @BeforeEach
    fun setUp() {
        Mockito.`when`(clock.zone).thenReturn(ClockProvider.ZONE_ID)
        Mockito.`when`(clock.instant()).thenReturn(mockedDateTime)
    }

    @Test
    fun `should create a complaint with country code fetched`() {
        val reporterId = UUID.randomUUID().toString()
        val productId = UUID.randomUUID().toString()
        val request = """
            {
                "reporterId": "$reporterId",
                "productId": "$productId",
                "content": "some complain comment"
            }""".trimIndent()

        val httpHeaders = HttpHeaders().also { it.add("X-Forwarded-For", "24.48.0.1") }
        val result = mvc.perform(postRawRequest(baseUrl, request).headers(httpHeaders))

        result.andExpect(status().isCreated)
        complaintDbAsserts
            .assertThatComplaint(reporterId, productId)
            .hasCounter(1)
            .hasContent("some complain comment")
            .hasCountry("Canada")
    }

    @Test
    fun `should create a complaint`() {
        val reporterId = UUID.randomUUID().toString()
        val productId = UUID.randomUUID().toString()
        val request = """
            {
                "reporterId": "$reporterId",
                "productId": "$productId",
                "content": "some complain comment"
            }""".trimIndent()

        val result = mvc.perform(postRawRequest(baseUrl, request))

        result.andExpect(status().isCreated)
        complaintDbAsserts
            .assertThatComplaint(reporterId, productId)
            .hasCounter(1)
            .hasContent("some complain comment")
            .hasCountry(null)
    }

    @Test
    fun `should increase the counter on creation the same complaint`() {
        val reporterId = UUID.randomUUID().toString()
        val productId = UUID.randomUUID().toString()
        val request = """
            {
                "reporterId": "$reporterId",
                "productId": "$productId",
                "content": "some other complain comment"
            }""".trimIndent()

        mvc.perform(postRawRequest(baseUrl, request)).andExpect(status().isCreated)
        mvc.perform(postRawRequest(baseUrl, request)).andExpect(status().isCreated)

        complaintDbAsserts
            .assertThatComplaint(reporterId, productId)
            .hasCounter(2)
            .hasContent("some other complain comment")
            .hasCountry(null)
    }

    @Test
    fun `should update the complaint content`() {
        val reporterId = UUID.randomUUID().toString()
        val productId = UUID.randomUUID().toString()
        createComplaint(reporterId, productId)
        val patchRequest = """
            {
                "reporterId": "$reporterId",
                "productId": "$productId",
                "content": "updated complain comment"
            }""".trimIndent()

        mvc.perform(patchRawRequest(baseUrl, patchRequest)).andExpect(status().isNoContent)
    }

    @Test
    fun `should return paged complaints`() {
        val reporterId1 = UUID.randomUUID().toString()
        val reporterId2 = UUID.randomUUID().toString()
        createComplaint(reporterId = reporterId1)
        createComplaint(reporterId = reporterId2)

        val response = mvc.perform(getRequest(baseUrl))
            .andReturn()
            .toGenericTypeResponse(pagedComplaintType)

        assertThat(response).isEqualTo(
            PagedResponse(
                elements = listOf(
                    Complaint(
                        reporterId = reporterId1,
                        productId = "2e997077-427b-4f8a-903f-6b344a713096",
                        content = "some complain comment",
                        createdDateTime = mockedDateTime
                    ),
                    Complaint(
                        reporterId = reporterId2,
                        productId = "2e997077-427b-4f8a-903f-6b344a713096",
                        content = "some complain comment",
                        createdDateTime = mockedDateTime
                    )
                ),
                pageData = PageData(
                    page = 0,
                    pageSize = 20,
                    totalItems = 2L,
                    totalPages = 1
                )
            )
        )
    }

    @Test
    fun `should return bad request on missing complaint for content patch`() {
        val reporterId = UUID.randomUUID().toString()
        val productId = UUID.randomUUID().toString()
        val patchRequest = """
            {
                "reporterId": "$reporterId",
                "productId": "$productId",
                "content": "updated complain comment"
            }""".trimIndent()

        val result = mvc.perform(patchRawRequest(baseUrl, patchRequest))
            .andExpect(status().isNotFound)

        assertThat(result.andReturn().toExceptionResponse())
            .isEqualTo(
                ExceptionResponse("Cannot find complaint")
            )
    }

    @Test
    fun `should return error on missing field during complaint creation`() {
        val request = """
            {
                "reporterId": "4b332efd-7f08-42a9-81aa-ab6aa19cbd06",
                "content": "some complain comment"
            }""".trimIndent()

        val response = mvc.perform(postRawRequest(baseUrl, request))
            .andExpect(status().isBadRequest)
            .andReturn()
            .toExceptionResponse()

        assertThat(response)
            .isEqualTo(
                ExceptionResponse("""
                    Received invalid request.
                    Details:
                    Required field: [productId] is missing""".trimIndent()
                )
            )
    }

    @Test
    fun `should return error on empty content during complaint creation`() {
        val request = """
            {
                "reporterId": "4b332efd-7f08-42a9-81aa-ab6aa19cbd06",
                "productId": "33a4038a-3f10-46a2-baf8-e3c055be73f3",
                "content": ""
            }""".trimIndent()

        val response = mvc.perform(postRawRequest(baseUrl, request))
            .andExpect(status().isBadRequest)
            .andReturn()
            .toExceptionResponse()

        assertThat(response)
            .isEqualTo(
                ExceptionResponse("""
                    Received invalid request.
                    Details:
                    invalid fields [1]: content=""".trimIndent()
                )
            )
    }

    @Test
    fun `should return error on missing field during complaint content update`() {
        val patchRequest = """
            {
                "reporterId": "4b332efd-7f08-42a9-81aa-ab6aa19cbd06",
                "productId": "2e997077-427b-4f8a-903f-6b344a713096"
            }""".trimIndent()

        val response = mvc.perform(patchRawRequest(baseUrl, patchRequest))
            .andExpect(status().isBadRequest)
            .andReturn()
            .toExceptionResponse()

        assertThat(response)
            .isEqualTo(
                ExceptionResponse("""
                    Received invalid request.
                    Details:
                    Required field: [content] is missing""".trimIndent()
                )
            )
    }

    private fun createComplaint(
        reporterId: String = "33a4038a-3f10-46a2-baf8-e3c055be73f3",
        productId: String = "2e997077-427b-4f8a-903f-6b344a713096",
        content: String = "some complain comment"
    ) {
        val createRequest = """
            {
                "reporterId": "$reporterId",
                "productId": "$productId",
                "content": "$content"
            }""".trimIndent()
        mvc.perform(postRawRequest(baseUrl, createRequest)).andExpect(status().isCreated)
    }
}