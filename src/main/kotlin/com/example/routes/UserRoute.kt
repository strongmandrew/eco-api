package com.example.routes

import com.example.domain.usecase.user.*
import com.example.domain.usecase.userTakeOff.GetAllUserTakeOffs
import com.example.domain.usecase.userTakeOff.GetTotalUserTakeOff
import com.example.entity.AuthUser
import com.example.entity.EmailCodeApprove
import com.example.entity.EmailSend
import com.example.entity.User
import com.example.utils.Const.DEFAULT_ID
import com.example.utils.respondWithCode
import io.ktor.server.application.*
import io.ktor.server.request.*
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

        get {

            val email = call.request.queryParameters["email"]

            call.respondWithCode {
                getUserByEmail(email ?: "")
            }
        }

        route(Endpoint.REGISTER.path) {

            post {

                val user = call.receive<User>()

                call.respondWithCode {
                    registerUser(user)
                }
            }
        }

        route(Endpoint.AUTHORIZE.path) {

            post {

                val user = call.receive<AuthUser>()

                call.respondWithCode {
                    authorizeUser(
                        user.email, user
                            .password
                    )
                }
            }

        }

        route("/{id}") {

            route(Endpoint.TAKE_OFF.path) {

                get {
                    val id = call.parameters["id"]
                    call.respondWithCode {
                        getAllUserTakeOffs(id?.toInt() ?: DEFAULT_ID)
                    }
                }

                get(Endpoint.TOTAL.path) {
                    val id = call.parameters["id"]
                    call.respondWithCode {
                        getTotalUserTakeOff(id?.toInt() ?: DEFAULT_ID)
                    }
                }
            }
        }

        route(Endpoint.VALIDATE.path) {

            route(Endpoint.SEND.path) {

                post {

                    val validateEmail = call.receive<EmailSend>()

                    call.respondWithCode {
                        sendValidation(validateEmail.email)
                    }
                }
            }

            route(Endpoint.APPROVE.path) {

                post {

                    val compareEmailCode = call
                        .receive<EmailCodeApprove>()

                    call.respondWithCode {
                        approveValidation(
                            compareEmailCode.email,
                            compareEmailCode.code
                        )
                    }
                }
            }

        }

    }

}