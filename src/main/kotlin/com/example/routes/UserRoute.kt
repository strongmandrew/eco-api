package com.example.routes

import com.example.domain.usecase.user.*
import com.example.domain.usecase.userTakeOff.GetAllUserTakeOffs
import com.example.domain.usecase.userTakeOff.GetTotalUserTakeOff
import com.example.entity.*
import com.example.utils.Const.DEFAULT_ID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
    val changePasswordUseCase: ChangePasswordUseCase by inject()
    val deleteUser: DeleteUser by inject()

    val getAllUserTakeOffs: GetAllUserTakeOffs by inject()
    val getTotalUserTakeOff: GetTotalUserTakeOff by inject()

    val getAllBlacklisted: GetAllBlacklisted by inject()
    val addToBlackListEmail: AddToBlacklist by inject()
    val removeEmailFromBlackList: RemoveFromBlacklist by inject()

    val getAllUserReviews: GetAllUserReviews by inject()

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

        authenticate("user-auth") {
            route(Endpoint.REVIEW.path) {
                get {
                    val principal = call.principal<JWTPrincipal>()
                    val uid =
                        principal!!.payload.getClaim("uid").asInt()
                    val response =
                        getAllUserReviews(uid)
                    call.respond(
                        message = response,
                        status = HttpStatusCode.fromValue(response.statusCode)
                    )
                }
            }
        }

        authenticate("admin-auth") {

            route(Endpoint.BLACKLIST.path) {

                get {
                    val response = getAllBlacklisted()
                    call.respond(
                        message = response,
                        status = HttpStatusCode.fromValue(response.statusCode)
                    )
                }

                post {
                    val email = call.request.queryParameters["email"]
                        ?: return@post
                    val response = addToBlackListEmail(email)
                    call.respond(
                        message = response,
                        status = HttpStatusCode.fromValue(response.statusCode)
                    )
                }

                delete {
                    val email = call.request.queryParameters["email"]
                        ?: return@delete
                    val response = removeEmailFromBlackList(email)
                    call.respond(
                        message = response,
                        status = HttpStatusCode.fromValue(response.statusCode)
                    )
                }
            }
        }

        route("/{id}") {

            authenticate("admin-auth") {
                delete {
                    val id = call.parameters["id"]

                    val response = deleteUser(id?.toInt() ?:
                    DEFAULT_ID)

                    call.respond(
                        message = response,
                        status = HttpStatusCode.fromValue(response.statusCode)
                    )
                }
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

            route(Endpoint.PROFILE.path) {

                patch(Endpoint.CHANGE_PASSWORD.path) {
                    val principal = call.principal<JWTPrincipal>()
                    val id =
                        principal!!.payload.getClaim("uid").asInt()

                    val password = call.receive<ChangePassword>()
                    val result = changePasswordUseCase(
                        id, password
                    )
                    call.respond(
                        message = result,
                        status = HttpStatusCode.fromValue(result.statusCode)
                    )

                }
            }

            route(Endpoint.TAKE_OFF.path) {

                get {
                    val principal = call.principal<JWTPrincipal>()
                    val id =
                        principal!!.payload.getClaim("uid").asInt()
                    val result = getAllUserTakeOffs(
                        id?.toInt() ?: DEFAULT_ID
                    )
                    call.respond(
                        message = result,
                        status = HttpStatusCode.fromValue(result.statusCode)
                    )
                }

                get(Endpoint.TOTAL.path) {
                    val principal = call.principal<JWTPrincipal>()
                    val id =
                        principal!!.payload.getClaim("uid").asInt()
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
    }
}