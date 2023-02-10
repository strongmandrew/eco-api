package com.example.domain

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val data: T? = null,
    val statusCode: Int,
    val error: ErrorResponse? = null
)


@Serializable
data class ErrorResponse(val name: String, val description: String)
