package com.example.routes

import com.example.domain.usecase.rubbishType.DeleteRubbishTypeById
import com.example.domain.usecase.rubbishType.GetRubbishTypeById
import com.example.domain.usecase.rubbishType.GetTotalRubbishTypeTakeOff
import com.example.domain.usecase.rubbishType.InsertRubbishType
import com.example.entity.RubbishType
import com.example.utils.Const.DEFAULT_ID
import com.example.utils.respondWithCode
import io.ktor.server.application.*
import io.ktor.server.request.*
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
            call.respondWithCode {
                getTotalRubbishTypeTakeOff(name)
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]
            call.respondWithCode {
                getRubbishTypeById(
                    id?.toInt() ?: DEFAULT_ID
                )
            }
        }

        post {
            val rubbishType = call.receive<RubbishType>()
            call.respondWithCode {
                insertRubbishType(rubbishType)
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]
            call.respondWithCode {
                deleteRubbishTypeById(
                    id?.toInt() ?: DEFAULT_ID
                )
            }
        }

    }


}