package com.example.domain

import com.example.utils.DevOnly
import kotlinx.serialization.Serializable

@Serializable
data class Response<out T>(
    val data: T? = null,
    val statusCode: Int,
    val error: ErrorResponse? = null
) {

    companion object {

        @DevOnly
        fun toDoResponse(): Response<Nothing> {
            return Response(
                statusCode = 501 // Not implemented
            )
        }
    }
}




@Serializable
data class ErrorResponse(val name: String, val description: String)
