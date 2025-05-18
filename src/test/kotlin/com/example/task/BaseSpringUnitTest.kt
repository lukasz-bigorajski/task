package com.example.task

import com.example.task.presentation.api.Complaint
import com.example.task.presentation.api.ExceptionResponse
import com.example.task.presentation.api.PagedResponse
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import javax.sql.DataSource

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration::class)
abstract class BaseSpringUnitTest {
    val pagedComplaintType: TypeReference<PagedResponse<Complaint>> =
        object : TypeReference<PagedResponse<Complaint>>() {}

    @Autowired
    lateinit var mvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper
    @Autowired
    lateinit var dataSource: DataSource

    fun postRawRequest(url: String, reqJson: String): MockHttpServletRequestBuilder {
        return post(url)
            .content(reqJson)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    }


    fun patchRawRequest(url: String, reqJson: String): MockHttpServletRequestBuilder {
        return patch(url)
            .content(reqJson)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    }

    fun getRequest(url: String): MockHttpServletRequestBuilder {
        return get(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    }

    fun MvcResult.toExceptionResponse(): ExceptionResponse =
        objectMapper.readValue(this.response.contentAsString, ExceptionResponse::class.java)

    fun <T> MvcResult.toGenericTypeResponse(typeRef: TypeReference<T>): T {
        return objectMapper.readValue(this.response.contentAsString, typeRef)
    }
}
