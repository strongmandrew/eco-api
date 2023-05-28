package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.domain.dao.UserDao
import com.example.domain.usecase.user.jwt.JWTHandler
import com.example.domain.usecase.user.jwt.Role
import com.example.utils.EnvProvider
import com.example.utils.ServiceResult
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val userDao: UserDao by inject()

    install(Authentication) {

        jwt("user-auth") {
            realm = "User jwt authentication"

            verifier {
                JWT.require(
                    Algorithm.HMAC256(
                        EnvProvider.jwtSecret
                    )
                ).withIssuer(EnvProvider.jwtIssuer)
                    .build()
            }

            validate {

                val userId = it.getClaim(
                    JWTHandler
                        .JWT_USER_ID, Int::class
                )

                val roleId = it.getClaim(
                    JWTHandler.JWT_ROLE_ID,
                    Int::class
                )

                val timesChanged = it.getClaim(
                    JWTHandler
                        .JWT_TIMES_CHANGED, Int::class
                )

                val user = userDao.getUserById(
                    userId ?: return@validate null
                )

                return@validate when (user) {

                    is ServiceResult.Success -> {
                        if (userId == user.data.id &&
                            (roleId == Role.USER.id || roleId ==
                                    Role.ADMIN.id) &&
                            user.data.timesChanged == timesChanged
                        ) {
                            JWTPrincipal(it.payload)
                        } else
                            null
                    }

                    is ServiceResult.Error -> {
                        null
                    }
                }

            }
        }

        jwt("admin-auth") {
            realm = "Admin jwt authentication"

            verifier {
                JWT.require(
                    Algorithm.HMAC256(EnvProvider.jwtSecret)
                ).withIssuer(EnvProvider.jwtIssuer)
                    .build()
            }

            validate {

                val userId = it.getClaim(
                    JWTHandler
                        .JWT_USER_ID, Int::class
                )

                val roleId = it.getClaim(
                    JWTHandler.JWT_ROLE_ID,
                    Int::class
                )

                val timesChanged = it.getClaim(
                    JWTHandler
                        .JWT_TIMES_CHANGED, Int::class
                )

                val user = userDao.getUserById(
                    userId ?: return@validate null
                )

                return@validate when (user) {

                    is ServiceResult.Success -> {
                        if (userId == user.data.id &&
                            roleId == Role.ADMIN.id &&
                            user.data.timesChanged == timesChanged
                        ) {
                            JWTPrincipal(it.payload)
                        } else
                            null
                    }

                    is ServiceResult.Error -> {
                        null
                    }
                }
            }
        }
    }

}
