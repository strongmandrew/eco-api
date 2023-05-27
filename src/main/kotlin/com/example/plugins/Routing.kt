package com.example.plugins

import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        swaggerUI(path = "/docs", swaggerFile = "./documentation/doc" +
                ".yaml")
        route(Endpoint.API.path) {
            homeRoute()
            userRoute()
            recyclePointRoute()
            reviewRoute()
            rubbishTypeRoute()
            userTakeOffRoute()
        }
    }

}
