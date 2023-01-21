package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.loginRoute() {

    routing {

        route(Endpoint.LOGIN.path) {

            get("/{id}") {

            }

            post {

            }

        }
    }
}