package com.example.routes

import com.example.domain.usecase.review.DeleteReviewById
import com.example.domain.usecase.review.GetReviewById
import com.example.utils.Const.DEFAULT_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.reviewRoute() {

    val getReviewById: GetReviewById by inject()
    val deleteReviewById: DeleteReviewById by inject()

    route("${Endpoint.REVIEW.path}/{id}") {

        get {
            val id = call.parameters["id"]

            val response = getReviewById(id?.toInt() ?: DEFAULT_ID)
            call.respond(message = response, status = HttpStatusCode.fromValue(response.statusCode))

        }

        delete {

            val id = call.parameters["id"]

            val response = deleteReviewById(id?.toInt() ?: DEFAULT_ID)
            call.respond(message = response, status = HttpStatusCode.fromValue(response.statusCode))

        }
    }

}