package com.example.routes

import com.example.domain.usecase.review.DeleteReviewById
import com.example.domain.usecase.review.GetReviewById
import com.example.utils.Const.DEFAULT_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.reviewRoute() {

    val getReviewById: GetReviewById by inject()
    val deleteReviewById: DeleteReviewById by inject()

    route("${Endpoint.REVIEW.path}/{id}") {

        authenticate("user-auth") {
            get {
                val id = call.parameters["id"]
                val result = getReviewById(id?.toInt() ?: DEFAULT_ID)
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )
            }
        }

        authenticate("admin-auth") {
            delete {
                val id = call.parameters["id"]
                val result =
                    deleteReviewById(id?.toInt() ?: DEFAULT_ID)
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )
            }
        }
    }

}