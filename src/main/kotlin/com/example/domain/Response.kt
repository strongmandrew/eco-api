package com.example.domain

import com.example.entity.User
import com.example.utils.DevOnly
import com.example.utils.Errors
import kotlinx.serialization.Serializable

@Serializable
data class Response<out T>(
    val data: T? = null,
    val statusCode: Int,
    val error: ErrorResponse? = null,
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

val NOTALLOWEDResponse = with(Errors.ACTION_NOT_ALLOWED) {
    Response<Boolean>(
        error = ErrorResponse(
            name, message
        ),
        statusCode = statusCode
    )
}

val EMAILEXISTSResponse = with(Errors.EMAIL_ALREADY_EXISTS) {
    Response<User>(
        error = ErrorResponse(
            name, message
        ),
        statusCode = statusCode
    )
}

val BADREQUESTResponse = with(Errors.BAD_INPUT) {
    Response<Boolean>(
        error = ErrorResponse(
            name, message
        ),
        statusCode = statusCode
    )
}

val PASSWORDSMISMATCHResponse = with(Errors.PASSWORDS_MISMATCH) {
    Response<String>(
        error = ErrorResponse(
            name, message
        ),
        statusCode = statusCode
    )
}

val INCORRECTPASSWORDResponse = with(Errors.INCORRECT_PASSWORD) {
    Response<String>(
        error = ErrorResponse(
            name, message
        ),
        statusCode = statusCode
    )
}

val INCORRECTMETHODResponse = with (Errors.METHOD_NOT_ALLOWED) {
    Response<String>(
        error = ErrorResponse(name, message),
        statusCode = statusCode
    )
}