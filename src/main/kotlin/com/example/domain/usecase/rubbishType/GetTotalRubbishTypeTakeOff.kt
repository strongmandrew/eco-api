package com.example.domain.usecase.rubbishType

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RubbishTypeDao
import com.example.utils.ServiceResult

class GetTotalRubbishTypeTakeOff(
    private val rubbishTypeDao: RubbishTypeDao
) {
    suspend operator fun invoke(idRubbishType: String):
            Response<Double> {

        return when (val total = rubbishTypeDao
            .getTotalRubbishTypeTakeOffByName(idRubbishType)) {

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