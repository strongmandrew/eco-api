package com.example.routes

import com.example.domain.usecase.recyclePoint.*
import com.example.domain.usecase.review.GetReviewsByPointId
import com.example.domain.usecase.review.InsertReview
import com.example.entity.RecyclePoint
import com.example.entity.Review
import com.example.utils.Const
import com.example.utils.Const.DEFAULT_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Route.recyclePointRoute() {

    val getRecyclePoints: GetRecyclePoints by inject()
    val getRecyclePointById: GetRecyclePointById by inject()
    val setRecyclePointPhoto: SetRecyclePointPhoto by inject()
    val changeRecyclePointApproval: ChangeRecyclePointApproval by inject()
    val insertRecyclePoint: InsertRecyclePoint by inject()
    val deleteRecyclePoint: DeleteRecyclePoint by inject()
    val getRecyclePointByQuery: GetRecyclePointByQuery by inject()
    val getRecyclePointsFilteredByType: GetPointsFilteredByType by inject()

    val getReviewsByPointId: GetReviewsByPointId by inject()
    val insertReview: InsertReview by inject()

    route(Endpoint.RECYCLE_POINT.path) {

        authenticate("user-auth") {
            route(Endpoint.PHOTO.path) {

                static {
                    staticRootFolder = File(Const.PHOTO_PATH)
                    files(".")
                }
            }

            get {

                val query = call.request.queryParameters["query"]
                val filter = call.request.queryParameters["filter"]

                val queried = query?.let { queried ->
                    getRecyclePointByQuery(queried)
                        .data?.toSet()
                }

                val filtered = filter?.let { filtered ->
                    getRecyclePointsFilteredByType(filtered)
                        .data?.toSet()
                }


                return@get when {
                    queried == null && filtered == null -> {
                        val result = getRecyclePoints()
                        return@get call.respond(
                            message = result,
                            status = HttpStatusCode.fromValue(result.statusCode)
                        )
                    }

                    queried == null ->
                        call.respond(
                            message = filtered!!,
                            status = HttpStatusCode.OK
                        )

                    filtered == null -> call.respond(
                        message = queried,
                        status = HttpStatusCode.OK
                    )


                    else -> call.respond(
                        message = filtered.intersect(queried),
                        status = HttpStatusCode.OK
                    )
                }
            }

            post {

                val point = call.receive<RecyclePoint>()
                val result = insertRecyclePoint(point)
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )

            }
        }

        route("/{id}") {

            authenticate("admin-auth") {
                patch(Endpoint.APPROVE.path) {

                    val id = call.parameters["id"]
                    val result = changeRecyclePointApproval(
                        id?.toInt() ?: DEFAULT_ID
                    )
                    call.respond(
                        message = result,
                        status = HttpStatusCode.fromValue(result.statusCode)
                    )
                }

                delete {
                    val id = call.parameters["id"]
                    val result =
                        deleteRecyclePoint(id?.toInt() ?: DEFAULT_ID)
                    call.respond(
                        message = result,
                        status = HttpStatusCode.fromValue(result.statusCode)
                    )
                }
            }

            authenticate("user-auth") {
                get {

                    val id = call.parameters["id"]
                    val result =
                        getRecyclePointById(id?.toInt() ?: DEFAULT_ID)
                    call.respond(
                        message = result,
                        status = HttpStatusCode.fromValue(result.statusCode)
                    )
                }

                route(Endpoint.PHOTO.path) {

                    patch {

                        val id = call.parameters["id"]
                        val extension = call.request
                            .queryParameters["ext"]

                        val channel = call.receiveChannel()

                        val result = setRecyclePointPhoto(
                            channel,
                            extension
                                ?: throw IllegalStateException(),
                            id?.toInt() ?: DEFAULT_ID
                        )
                        call.respond(
                            message = result,
                            status = HttpStatusCode.fromValue(result.statusCode)
                        )
                    }

                }

                route(Endpoint.REVIEW.path) {

                    get {

                        val id = call.parameters["id"]

                        val result = getReviewsByPointId(
                            id?.toInt() ?: DEFAULT_ID
                        )
                        call.respond(
                            message = result,
                            status = HttpStatusCode.fromValue(result.statusCode)
                        )
                    }

                    post {

                        val review = call.receive<Review>()

                        val id = call.parameters["id"]
                        val result = insertReview(
                            review,
                            id?.toInt() ?: DEFAULT_ID
                        )
                        call.respond(
                            message = result,
                            status = HttpStatusCode.fromValue(result.statusCode)
                        )
                    }
                }
            }
        }
    }

}