package com.example.domain.usecase.userTakeOff

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserTakeOffDao
import com.example.utils.ServiceResult

class DeleteTakeOffById(
    private val userTakeOffDao: UserTakeOffDao
) {
    suspend operator fun invoke(takeoffId: Int): Response<Boolean> {
        return when (val revert = userTakeOffDao
            .revertTakeOff(takeoffId)) {

            is ServiceResult.Success -> {
                Response(
                    data = revert.data,
                    statusCode = 200
                )
            }
            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        revert.error.name, revert.error.message
                    ),
                    statusCode = revert.error.statusCode
                )
            }
        }
    }
}