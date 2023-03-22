package com.example.domain.usecase.userTakeOff

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserTakeOffDao
import com.example.entity.UserTakeOff
import com.example.utils.ServiceResult

class GetTakeOffById(
    private val userTakeOffDao: UserTakeOffDao
) {
    suspend operator fun invoke(takeoffId: Int):
            Response<UserTakeOff> {

        return when (val takeoff = userTakeOffDao
            .getTakeOffById(takeoffId)) {

            is ServiceResult.Success -> {
                Response(
                    data = takeoff.data,
                    statusCode = 200
                )
            }
            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        takeoff.error.name, takeoff.error.message
                    ),
                    statusCode = takeoff.error.statusCode
                )
            }
        }
    }
}