package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserDao
import com.example.domain.usecase.user.jwt.JWTHandler
import com.example.utils.ServiceResult

class ApproveValidation(
    private val userDao: UserDao,
    private val jwtHandler: JWTHandler
) {
    suspend operator fun invoke(email: String, inputCode: Int):
            Response<String> {

        return when (val compare = userDao.compareValidationCode
            (email, inputCode)) {

            is ServiceResult.Success -> {

                when (val change = userDao.approveUserEmail(email)) {

                    is ServiceResult.Success -> {

                        when (val user = userDao.getUserByEmail
                            (email)) {

                            is ServiceResult.Success -> {
                                val successUser = user.data
                                Response(
                                    jwtHandler.generateToken(
                                        successUser.id!!,
                                        successUser.role!!,
                                        successUser.timesChanged!!
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
                                change.error.name, change.error.message
                            ),
                            statusCode = change.error.statusCode
                        )
                    }
                }
            }
            is ServiceResult.Error -> {

                Response(
                    error = ErrorResponse(
                        compare.error.name, compare.error.message
                    ),
                    statusCode = compare.error.statusCode
                )
            }
        }

    }
}