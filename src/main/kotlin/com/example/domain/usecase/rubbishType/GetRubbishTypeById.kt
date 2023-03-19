package com.example.domain.usecase.rubbishType

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RubbishTypeDao
import com.example.entity.RubbishType
import com.example.utils.ServiceResult

class GetRubbishTypeById(
    private val rubbishTypeDao: RubbishTypeDao
) {
    suspend operator fun invoke(idRubbishType: Int): Response<RubbishType> {
        return when(val type = rubbishTypeDao
            .getRubbishTypeById(idRubbishType)) {

            is ServiceResult.Success -> {
                Response(
                    data = type.data,
                    statusCode = 200
                )
            }

            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        type.error.name, type.error.message
                    ),
                     statusCode = type.error.statusCode
                )
            }
        }
    }
}