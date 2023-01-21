package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.newsRoute() {
    routing {

        route(Endpoint.NEWS.path) {

            get {


            }

        }
    }
}