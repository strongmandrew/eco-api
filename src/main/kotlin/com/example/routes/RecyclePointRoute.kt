package com.example.routes

import com.example.data.RecyclePointDaoImpl
import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.usecase.recyclePoint.*
import com.example.entity.RecyclePoint
import com.example.utils.Const.DEFAULT_ID
import com.example.utils.Errors
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import org.koin.ktor.ext.inject

fun Routing.recyclePointRoute() {

    val getRecyclePoints: GetRecyclePoints by inject()
    val getRecyclePointById: GetRecyclePointById by inject()
    val setRecyclePointPhoto: SetRecyclePointPhoto by inject()
    val changeRecyclePointApproval: ChangeRecyclePointApproval by inject()
    val insertRecyclePoint: InsertRecyclePoint by inject()
    val deleteRecyclePoint: DeleteRecyclePoint by inject()

    route(Endpoint.RECYCLE_POINT.path) {


        get {
            val response = getRecyclePoints()

            call.respond(response)

        }

        post {

            val point = call.receive<RecyclePoint>()
            val response = insertRecyclePoint(point)

            call.respond(response)

        }

        route("/{id}") {

            patch(Endpoint.APPROVE.path) {

                val id = call.parameters["id"]

                val response = changeRecyclePointApproval(id?.toInt() ?: DEFAULT_ID)

                call.respond(response)

            }

            get {

                val id = call.parameters["id"]

                val response = getRecyclePointById(id?.toInt() ?: DEFAULT_ID)
                call.respond(response)

            }

            delete {
                val id = call.parameters["id"]
                val response = deleteRecyclePoint(id?.toInt() ?: DEFAULT_ID)

                call.respond(response)
            }

            route(Endpoint.PHOTO.path) {

                patch {

                    val id = call.parameters["id"]

                    var response = Response<Boolean>(error = ErrorResponse(Errors.DATABASE_ERROR
                        .name,
                        Errors.DATABASE_ERROR.message))

                    val multipart = call.receiveMultipart()
                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                response = setRecyclePointPhoto(part.streamProvider()
                                    .readBytes(), id?.toInt() ?: DEFAULT_ID )
                            }
                            else -> {}
                        }
                        part.dispose()
                    }

                    call.respond(response)

                }
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