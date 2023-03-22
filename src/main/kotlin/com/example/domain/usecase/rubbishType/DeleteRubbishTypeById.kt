package com.example.domain.usecase.rubbishType

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RubbishTypeDao
import com.example.utils.ServiceResult

class DeleteRubbishTypeById(
    private val rubbishTypeDao: RubbishTypeDao
) {
    suspend operator fun invoke(rubbishTypeId: Int):
            Response<Boolean> {
        return when (val delete = rubbishTypeDao
            .deleteRubbishTypeById(rubbishTypeId)) {

            is ServiceResult.Success -> {
                Response(
                    data = delete.data,
                    statusCode = 200
                )
            }

            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        delete.error.name, delete.error.message
                    ),
                    statusCode = delete.error.statusCode
                )
            }
        }
    }
}