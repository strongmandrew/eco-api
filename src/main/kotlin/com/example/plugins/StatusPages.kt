package com.example.plugins

import com.example.domain.BADREQUESTResponse
import com.example.domain.INCORRECTMETHODResponse
import com.example.domain.NOTALLOWEDResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond(NOTALLOWEDResponse)
        }
        status(HttpStatusCode.BadRequest) { call, _ ->
            call.respond(BADREQUESTResponse)
        }
        status(HttpStatusCode.MethodNotAllowed) { call, _ ->
            call.respond(INCORRECTMETHODResponse)
        }
    }
}