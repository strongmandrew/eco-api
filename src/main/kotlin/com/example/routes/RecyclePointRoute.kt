package com.example.routes

import com.example.domain.usecase.recyclePoint.*
import com.example.domain.usecase.review.GetReviewsByPointId
import com.example.domain.usecase.review.InsertReview
import com.example.entity.RecyclePoint
import com.example.entity.Review
import com.example.utils.Const
import com.example.utils.Const.DEFAULT_ID
import com.example.utils.respondWithCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
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

    val getReviewsByPointId: GetReviewsByPointId by inject()
    val insertReview: InsertReview by inject()

    authenticate("user-auth") {
        route(Endpoint.RECYCLE_POINT.path) {

            route(Endpoint.PHOTO.path) {

                static {
                    staticRootFolder = File(Const.PHOTO_PATH)
                    files(".")
                }
            }


            get {

                val query = call.request.queryParameters["query"]

                query?.let {
                    call.respondWithCode {
                        getRecyclePointByQuery(it)
                    }
                    return@get
                }

                call.respondWithCode {
                    getRecyclePoints()
                }

            }

            post {

                val point = call.receive<RecyclePoint>()
                call.respondWithCode {
                    insertRecyclePoint(point)
                }

            }

            route("/{id}") {

                patch(Endpoint.APPROVE.path) {

                    val id = call.parameters["id"]
                    call.respondWithCode {
                        changeRecyclePointApproval(
                            id?.toInt() ?: DEFAULT_ID
                        )
                    }
                }

                get {

                    val id = call.parameters["id"]

                    call.respondWithCode {
                        getRecyclePointById(id?.toInt() ?: DEFAULT_ID)
                    }
                }

                delete {
                    val id = call.parameters["id"]
                    call.respondWithCode {
                        deleteRecyclePoint(id?.toInt() ?: DEFAULT_ID)
                    }
                }

                route(Endpoint.PHOTO.path) {

                    patch {

                        val id = call.parameters["id"]
                        val extension = call.request
                            .queryParameters["ext"]

                        val channel = call.receiveChannel()

                        call.respondWithCode {
                            setRecyclePointPhoto(
                                channel,
                                extension
                                    ?: throw IllegalStateException(),
                                id?.toInt() ?: DEFAULT_ID
                            )
                        }
                    }

                }

                route(Endpoint.REVIEW.path) {

                    get {

                        val id = call.parameters["id"]

                        call.respondWithCode {
                            getReviewsByPointId(
                                id?.toInt() ?: DEFAULT_ID
                            )
                        }
                    }

                    post {

                        val review = call.receive<Review>()

                        val id = call.parameters["id"]

                        call.respondWithCode {
                            insertReview(
                                review,
                                id?.toInt() ?: DEFAULT_ID
                            )
                        }
                    }

                }
            }


        }

    }


}