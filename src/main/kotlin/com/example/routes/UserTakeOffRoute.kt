package com.example.routes

import com.example.domain.usecase.userTakeOff.DeleteTakeOffById
import com.example.domain.usecase.userTakeOff.GetTakeOffById
import com.example.domain.usecase.userTakeOff.TakeOffRubbish
import com.example.entity.UserTakeOff
import com.example.utils.Const.DEFAULT_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userTakeOffRoute() {

    val getTakeOffById: GetTakeOffById by inject()
    val takeOffRubbish: TakeOffRubbish by inject()
    val deleteTakeOffById: DeleteTakeOffById by inject()

    route(Endpoint.TAKE_OFF.path) {

        get("/{id}") {
            val id = call.parameters["id"]
            val result = getTakeOffById(id?.toInt() ?: DEFAULT_ID)
            call.respond(
                message = result,
                status = HttpStatusCode.fromValue(result.statusCode)
            )
        }

        post {
            val takeOff = call.receive<UserTakeOff>()
            val result = takeOffRubbish(takeOff)
            call.respond(
                message = result,
                status = HttpStatusCode.fromValue(result.statusCode)
            )
        }

        delete("/{id}") {
            val id = call.parameters["id"]
            val result = deleteTakeOffById(id?.toInt() ?: DEFAULT_ID)
            call.respond(
                message = result,
                status = HttpStatusCode.fromValue(result.statusCode)
            )
        }
    }
}