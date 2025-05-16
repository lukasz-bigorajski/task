package com.example.task.presentation.api

open class BusinessException(msg: String) : Exception(msg)
class NotFoundException(msg: String) : BusinessException(msg)
class ResourceException(msg: String) : Exception(msg)
