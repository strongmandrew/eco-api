package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.recyclePointRoute() {

    routing {
        route(Endpoint.RECYCLE_POINT.path) {

            get {

            }

            post {

            }

            route("/{id}") {

                put(Endpoint.APPROVE.path) {

                }

                put(Endpoint.DISAPPROVE.path) {


                }

                get {


                }

                route(Endpoint.REVIEW.path) {

                    get {

                    }

                    post {

                    }

                }
            }

        }

    }


}