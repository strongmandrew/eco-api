package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserDao
import com.example.utils.ServiceResult

class ApproveValidation(
    private val userDao: UserDao
) {
    suspend operator fun invoke(email: String, inputCode: Int):
            Response<Boolean> {

        return when (val compare = userDao.compareValidationCode
            (email, inputCode)) {

            is ServiceResult.Success -> {

                when (val change = userDao.approveUserEmail(email)) {

                    is ServiceResult.Success -> {
                        Response(
                            data = change.data,
                            statusCode = 200
                        )
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