package com.example.task.application

import com.example.task.presentation.api.BusinessException
import com.example.task.presentation.api.ExceptionResponse
import com.example.task.presentation.api.NotFoundException
import com.example.task.presentation.api.ResourceException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ExceptionResponse> {
        val response = ExceptionResponse(ex.message ?: "")
        return when(ex) {
            is NotFoundException -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
            else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }
    }

    @ExceptionHandler
    @ResponseBody
    fun handleResourceException(ex: ResourceException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ExceptionResponse(ex.message ?: ""))
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBindException(ex: MethodArgumentNotValidException): ExceptionResponse {
        val invalidFieldsMessage = ex.formatFieldErrorsMessage()
        return toInvalidFieldsMessageResponse(invalidFieldsMessage)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBindException(ex: HttpMessageNotReadableException): ExceptionResponse {
        val exceptionMessage = getHttpMessageExceptionDetails(ex)
        return toInvalidFieldsMessageResponse(exceptionMessage)
    }

    private fun toInvalidFieldsMessageResponse(invalidFieldsMessage: String): ExceptionResponse = ExceptionResponse(
        message = """
            Received invalid request.
            Details:
            $invalidFieldsMessage
        """.trimIndent()
    )

    private fun MethodArgumentNotValidException.formatFieldErrorsMessage(): String {
        val fieldErrors = this.bindingResult.fieldErrors
        return "invalid fields [${fieldErrors.size}]: " + fieldErrors.joinToString(separator = ",") {
            "${it.field}=${it.rejectedValue}"
        }
    }

    private fun getHttpMessageExceptionDetails(ex: HttpMessageNotReadableException): String =
        when (val cause = ex.cause) {
            is MissingKotlinParameterException ->
                "Required field: [${cause.parameter.name}] is missing"
            is InvalidFormatException ->
                "${cause.value} is not a valid value for object of type ${cause.targetType?.simpleName}"
            else -> "Http message is not readable"
        }

}
