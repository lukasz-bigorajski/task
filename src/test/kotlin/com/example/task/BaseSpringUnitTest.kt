package com.example.task

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@SpringBootTest
@AutoConfigureMockMvc
abstract class BaseSpringUnitTest {

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
}
