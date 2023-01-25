package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Routing.loginRoute() {

    route(Endpoint.LOGIN.path) {

        get("/{id}") {

        }

        post {

        }

    }


}