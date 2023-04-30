package com.example.routes

import com.example.domain.usecase.review.DeleteReviewById
import com.example.domain.usecase.review.GetReviewById
import com.example.utils.Const.DEFAULT_ID
import com.example.utils.respondWithCode
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.reviewRoute() {

    val getReviewById: GetReviewById by inject()
    val deleteReviewById: DeleteReviewById by inject()

    route("${Endpoint.REVIEW.path}/{id}") {

        get {
            val id = call.parameters["id"]

            call.respondWithCode {
                getReviewById(id?.toInt() ?: DEFAULT_ID)
            }

        }

        delete {

            val id = call.parameters["id"]

            call.respondWithCode {
                deleteReviewById(id?.toInt() ?: DEFAULT_ID)
            }
        }
    }

}