package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserDao
import com.example.entity.User
import com.example.utils.ServiceResult

class GetUserByEmail(
    private val userDao: UserDao
) {

    suspend operator fun invoke(email: String): Response<User> {

        return when (val user = userDao.getUserByEmail(email)) {

            is ServiceResult.Success -> {
                Response(
                    data = user.data,
                    statusCode = 200
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
}