package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Routing.newsRoute() {


    route(Endpoint.NEWS.path) {

        get {


        }

    }


}