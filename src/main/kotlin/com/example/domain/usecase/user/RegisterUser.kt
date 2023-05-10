package com.example.domain.usecase.user

import com.example.domain.EMAILEXISTSResponse
import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserDao
import com.example.entity.User
import com.example.utils.ServiceResult

class RegisterUser(
    private val userDao: UserDao,
) {
    suspend operator fun invoke(user: User): Response<User> {

        return when (val exist =
            userDao.emailDoesNotExist(user.email)) {

            is ServiceResult.Success -> {

                if (exist.data) {
                    when (val blacklist =
                        userDao.isEmailNotInBlacklist
                            (user.email)) {

                        is ServiceResult.Success -> {

                            when (val registry =
                                userDao.registerUser(user)) {

                                is ServiceResult.Success -> {

                                    Response(
                                        data = registry.data,
                                        statusCode = 201
                                    )
                                }

                                is ServiceResult.Error -> {
                                    Response(
                                        error = ErrorResponse(
                                            registry.error.name,
                                            registry.error.message
                                        ),
                                        statusCode = registry.error.statusCode
                                    )
                                }
                            }
                        }

                        is ServiceResult.Error -> {

                            Response(
                                error = ErrorResponse(
                                    blacklist.error.name,
                                    blacklist.error.message
                                ),
                                statusCode = blacklist.error.statusCode
                            )
                        }
                    }
                } else {
                    EMAILEXISTSResponse
                }
            }

            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        exist.error.name, exist.error.message
                    ),
                    statusCode = exist.error.statusCode
                )
            }
        }

    }
}