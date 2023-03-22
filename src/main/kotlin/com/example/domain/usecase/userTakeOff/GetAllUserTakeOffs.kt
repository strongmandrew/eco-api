package com.example.domain.usecase.userTakeOff

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserTakeOffDao
import com.example.entity.UserTakeOff
import com.example.utils.ServiceResult

class GetAllUserTakeOffs(
    private val userTakeOffDao: UserTakeOffDao
) {
    suspend operator fun invoke(userId: Int):
            Response<List<UserTakeOff>> {

        return when (val takeoffs = userTakeOffDao
            .getUserTakeOffById(userId)) {

            is ServiceResult.Success -> {
                Response(
                    data = takeoffs.data,
                    statusCode = 200
                )
            }
            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        takeoffs.error.name, takeoffs.error.message
                    ),
                    statusCode = takeoffs.error.statusCode
                )
            }

        }
    }
}