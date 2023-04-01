package com.example.plugins

import com.example.domain.BADREQUESTResponse
import com.example.domain.UNAUTHORIZEDResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond(UNAUTHORIZEDResponse)
        }
        status(HttpStatusCode.BadRequest) { call, _ ->
            call.respond(BADREQUESTResponse)
        }
    }
}