package com.example.routes

import com.example.domain.usecase.news.GetNews
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.newsRoute() {

    val getNews: GetNews by inject()

    route(Endpoint.NEWS.path) {

        get {

            val response = getNews()

            call.respond(message = response, status = HttpStatusCode.fromValue(response.statusCode))


        }

    }


}