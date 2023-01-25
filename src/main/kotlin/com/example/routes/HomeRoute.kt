package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.homeRoute() {


    get(Endpoint.HOME.path) {

        call.respondText { "Welcome to Eco! REST API.\nSUAI, 2023" }
    }




}
