package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserDao
import com.example.domain.usecase.user.jwt.JWTHandler
import com.example.utils.Const.DEFAULT_ID
import com.example.utils.ServiceResult

class AuthorizeUser(
    private val userDao: UserDao,
    private val jwtHandler: JWTHandler,
    private val passwordEncrypt: PasswordEncrypt
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Response<String> {

        return when (val exists = userDao.getUserByEmail(email)) {

            is ServiceResult.Success -> {

                when (val verified = userDao.userEmailVerified
                    (exists.data.id ?: DEFAULT_ID)) {

                    is ServiceResult.Success -> {

                        when (val user = userDao
                            .checkUserCredentials(email,
                                passwordEncrypt(password))) {

                            is ServiceResult.Success -> {
                                Response(
                                    data = jwtHandler.generateToken(
                                        userId = user.data.id!!,
                                        role = user.data.role!!,
                                        timesCredentialsChanged = user
                                            .data.timesChanged!!
                                    ),
                                    statusCode = 201
                                )
                            }

                            is ServiceResult.Error -> {

                                Response(
                                    error = ErrorResponse(
                                        user.error.name, user.error.message
                                    ),
                                    statusCode = user.error.statusCode
                                )
                            }
                        }


                    }

                    is ServiceResult.Error -> {
                        Response(
                            error = ErrorResponse(
                                verified.error.name, verified.error.message
                            ),
                            statusCode = verified.error.statusCode
                        )

                    }

                }

            }

            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        exists.error.name, exists.error.message
                    ),
                    statusCode = exists.error.statusCode
                )

            }


        }

    }
}