package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.USEREXISTSResponse
import com.example.domain.dao.BlacklistDao
import com.example.domain.dao.UserDao
import com.example.utils.ServiceResult

class AddToBlacklist(
    private val userDao: UserDao,
    private val blacklistDao: BlacklistDao
) {

    suspend operator fun invoke(email: String): Response<Boolean> {
        return when (val user = userDao.getUserByEmail(email)) {
            is ServiceResult.Success -> {
                val obtainedUser = user.data
                when (
                    val ban =
                        blacklistDao.blackListEmail(obtainedUser.email)
                ) {

                    is ServiceResult.Success -> Response(
                        data = ban.data,
                        statusCode = 201
                    )

                    is ServiceResult.Error -> Response(
                        error = ErrorResponse(
                            ban.error.name, ban.error.message
                        ),
                        statusCode = ban.error.statusCode
                    )
                }
            }

            is ServiceResult.Error -> USEREXISTSResponse
        }
    }

}