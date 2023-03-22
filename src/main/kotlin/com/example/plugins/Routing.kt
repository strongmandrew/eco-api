package com.example.plugins

import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        route(Endpoint.API.path) {
            homeRoute()
            userRoute()
            newsRoute()
            recyclePointRoute()
            reviewRoute()
            rubbishTypeRoute()
            userTakeOffRoute()
        }
    }

}
