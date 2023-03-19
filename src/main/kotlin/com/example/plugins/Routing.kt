package com.example.plugins

import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        homeRoute()
        userRoute()
        newsRoute()
        recyclePointRoute()
        reviewRoute()
        rubbishTypeRoute()
    }

}
