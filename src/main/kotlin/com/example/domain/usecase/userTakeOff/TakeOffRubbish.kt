package com.example.domain.usecase.userTakeOff

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserTakeOffDao
import com.example.entity.UserTakeOff
import com.example.utils.ServiceResult

class TakeOffRubbish(
    private val userTakeOffDao: UserTakeOffDao
) {
    suspend operator fun invoke(takeOff: UserTakeOff):
            Response<UserTakeOff> {
        return when (val rubbish = userTakeOffDao
            .takeOffRubbish(takeOff)) {

            is ServiceResult.Success -> {
                Response(
                    data = rubbish.data,
                    statusCode = 201
                )
            }
            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        rubbish.error.name, rubbish.error.message
                    ),
                    statusCode = rubbish.error.statusCode
                )
            }
        }
    }
}