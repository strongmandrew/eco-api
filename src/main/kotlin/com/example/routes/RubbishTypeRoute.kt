package com.example.routes

import com.example.domain.usecase.rubbishType.DeleteRubbishTypeById
import com.example.domain.usecase.rubbishType.GetRubbishTypeById
import com.example.domain.usecase.rubbishType.GetTotalRubbishTypeTakeOff
import com.example.domain.usecase.rubbishType.InsertRubbishType
import com.example.entity.RubbishType
import com.example.utils.Const.DEFAULT_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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

        authenticate("admin-auth") {
            get("/total") {
                val name = call.request.queryParameters["name"] ?: ""
                val result = getTotalRubbishTypeTakeOff(name)
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )
            }

            get("/{id}") {
                val id = call.parameters["id"]
                val result = getRubbishTypeById(
                    id?.toInt() ?: DEFAULT_ID
                )
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )
            }
        }

        authenticate("admin-auth") {
            post {
                val rubbishType = call.receive<RubbishType>()
                val result = insertRubbishType(rubbishType)
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )
            }

            delete("/{id}") {
                val id = call.parameters["id"]
                val result = deleteRubbishTypeById(
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