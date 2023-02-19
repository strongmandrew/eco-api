package com.example.routes

import com.example.domain.usecase.user.ApproveValidation
import com.example.domain.usecase.user.GetUserByEmail
import com.example.domain.usecase.user.SendValidation
import com.example.domain.usecase.user.RegisterUser
import com.example.entity.User
import com.example.entity.EmailCodeApprove
import com.example.entity.EmailSend
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.userRoute() {

    val registerUser: RegisterUser by inject()
    val sendValidation: SendValidation by inject()
    val approveValidation: ApproveValidation by inject()
    val getUserByEmail: GetUserByEmail by inject()

    route(Endpoint.USER.path) {

        get {

            val email = call.request.queryParameters["email"]

            val response = getUserByEmail(email ?: "")

            call.respond(message = response, status =
            HttpStatusCode.fromValue(response.statusCode))
        }

        route(Endpoint.REGISTER.path) {

            post {

                val user = call.receive<User>()

                val response = registerUser(user)

                call.respond(message = response, status =
                HttpStatusCode.fromValue(response.statusCode))


            }
        }

        route(Endpoint.VALIDATE.path) {

            route(Endpoint.SEND.path) {

                post {

                    val validateEmail = call.receive<EmailSend>()

                    val response =
                        sendValidation(validateEmail.email)

                    call.respond(
                        message = response, status =
                        HttpStatusCode.fromValue
                            (response.statusCode)
                    )

                }
            }

            route(Endpoint.APPROVE.path) {

                post {

                    val compareEmailCode = call
                        .receive<EmailCodeApprove>()

                    val response = approveValidation(
                        compareEmailCode.email,
                        compareEmailCode.code
                    )

                    call.respond(message = response, status =
                    HttpStatusCode.fromValue(
                        response.statusCode
                    ))
                }
            }
        }

    }

}