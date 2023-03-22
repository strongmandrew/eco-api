package com.example.routes

import com.example.domain.usecase.rubbishType.DeleteRubbishTypeById
import com.example.domain.usecase.rubbishType.GetRubbishTypeById
import com.example.domain.usecase.rubbishType.GetTotalRubbishTypeTakeOff
import com.example.domain.usecase.rubbishType.InsertRubbishType
import com.example.entity.RubbishType
import com.example.utils.Const.DEFAULT_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.rubbishTypeRoute() {

    val getRubbishTypeById: GetRubbishTypeById by inject()
    val getTotalRubbishTypeTakeOff: GetTotalRubbishTypeTakeOff by inject()
    val insertRubbishType: InsertRubbishType by inject()
    val deleteRubbishTypeById: DeleteRubbishTypeById by inject()

    route(Endpoint.RUBBISH_TYPE.path) {

        get("/total") {
            val name = call.request.queryParameters["name"] ?: ""
            val response = getTotalRubbishTypeTakeOff(name)
            call.respond(
                message = response,
                status = HttpStatusCode.fromValue(response.statusCode)
            )
        }

        get("/{id}") {
            val id = call.parameters["id"]
            val response = getRubbishTypeById(id?.toInt() ?:
            DEFAULT_ID)
            call.respond(
                message = response,
                status = HttpStatusCode.fromValue(response.statusCode)
            )
        }

        post {
            val rubbishType = call.receive<RubbishType>()
            val response = insertRubbishType(rubbishType)
            call.respond(
                message = response,
                status = HttpStatusCode.fromValue(response.statusCode)
            )
        }

        delete("/{id}") {
            val id = call.parameters["id"]
            val response = deleteRubbishTypeById(id?.toInt() ?:
            DEFAULT_ID)
            call.respond(
                message = response,
                status = HttpStatusCode.fromValue(response.statusCode)
            )
        }

    }


}