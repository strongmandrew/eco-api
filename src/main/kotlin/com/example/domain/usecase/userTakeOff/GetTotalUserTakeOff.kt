package com.example.domain.usecase.userTakeOff

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserTakeOffDao
import com.example.utils.ServiceResult

class GetTotalUserTakeOff(
    private val userTakeOffDao: UserTakeOffDao
) {
    suspend operator fun invoke(userId: Int): Response<Double> {

        return when(val total = userTakeOffDao
            .totalUserTakeOff(userId)) {

            is ServiceResult.Success -> {
                Response(
                    data = total.data,
                    statusCode = 200
                )
            }
            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        total.error.name, total.error.message
                    ),
                    statusCode = total.error.statusCode
                )
            }
        }
    }
}