package com.example.utils

import com.example.domain.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

/**
 * Wraps [Response] to `respond()` format
 * @param responseBlock action returning [Response]
 */
suspend fun <T> ApplicationCall.respondWithCode(
    responseBlock: suspend () -> Response<T>
) {
    val result = responseBlock.invoke()
    respond(
        message = result,
        status = HttpStatusCode.fromValue(result.statusCode)
    )
}