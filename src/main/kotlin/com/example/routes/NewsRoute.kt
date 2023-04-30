package com.example.routes

import com.example.domain.usecase.news.GetNews
import com.example.utils.respondWithCode
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.newsRoute() {

    val getNews: GetNews by inject()

    route(Endpoint.NEWS.path) {

        get {

            call.respondWithCode {
                getNews()
            }
        }

    }


}