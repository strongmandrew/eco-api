package com.example.routes

import com.example.domain.usecase.user.*
import com.example.domain.usecase.userTakeOff.GetAllUserTakeOffs
import com.example.domain.usecase.userTakeOff.GetTotalUserTakeOff
import com.example.entity.AuthUser
import com.example.entity.EmailCodeApprove
import com.example.entity.EmailSend
import com.example.entity.User
import com.example.utils.Const.DEFAULT_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {

    val registerUser: RegisterUser by inject()
    val sendValidation: SendValidation by inject()
    val approveValidation: ApproveValidation by inject()
    val getUserByEmail: GetUserByEmail by inject()
    val authorizeUser: AuthorizeUser by inject()

    val getAllUserTakeOffs: GetAllUserTakeOffs by inject()
    val getTotalUserTakeOff: GetTotalUserTakeOff by inject()

    route(Endpoint.USER.path) {

        route(Endpoint.REGISTER.path) {
            post {
                val user = call.receive<User>()

                val result = registerUser(user)
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )
            }
        }

        route(Endpoint.AUTHORIZE.path) {

            post {

                val user = call.receive<AuthUser>()

                val result = authorizeUser(
                    user.email, user
                        .password
                )
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )
            }

        }

        authenticate("user-auth") {
            get {

                val email = call.request.queryParameters["email"]

                val result = getUserByEmail(email ?: "")
                call.respond(
                    message = result,
                    status = HttpStatusCode.fromValue(result.statusCode)
                )
            }

            route("/{id}") {

                route(Endpoint.TAKE_OFF.path) {

                    get {
                        val id = call.parameters["id"]
                        val result = getAllUserTakeOffs(
                            id?.toInt() ?: DEFAULT_ID
                        )
                        call.respond(
                            message = result,
                            status = HttpStatusCode.fromValue(result.statusCode)
                        )
                    }

                    get(Endpoint.TOTAL.path) {
                        val id = call.parameters["id"]
                        val result = getTotalUserTakeOff(
                            id?.toInt() ?: DEFAULT_ID
                        )
                        call.respond(
                            message = result,
                            status = HttpStatusCode.fromValue(result.statusCode)
                        )
                    }
                }
            }

            route(Endpoint.VALIDATE.path) {
                route(Endpoint.SEND.path) {
                    post {

                        val validateEmail = call.receive<EmailSend>()
                        val result =
                            sendValidation(validateEmail.email)
                        call.respond(
                            message = result,
                            status = HttpStatusCode.fromValue(result.statusCode)
                        )
                    }
                }

                route(Endpoint.APPROVE.path) {

                    post {

                        val compareEmailCode = call
                            .receive<EmailCodeApprove>()

                        val result = approveValidation(
                            compareEmailCode.email,
                            compareEmailCode.code
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