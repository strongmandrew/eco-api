package com.example.domain

import com.example.utils.Errors
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val data: T? = null,
    val error: ErrorResponse? = null
)

@Serializable
data class ErrorResponse(val code: String, val description: String)
