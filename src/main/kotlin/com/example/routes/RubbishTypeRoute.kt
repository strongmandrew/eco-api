package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.rubbishTypeRoute() {
    routing {
        route(Endpoint.RUBBISH_TYPE.path) {

            post {

            }

        }
    }
}