package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.recyclePointRoute() {

    routing {
        route(Endpoint.RECYCLE_POINT.path) {

            post {

            }

            route("/{id}") {

                get {


                }

                get(Endpoint.REVIEW.path) {



                }
            }

        }

    }


}