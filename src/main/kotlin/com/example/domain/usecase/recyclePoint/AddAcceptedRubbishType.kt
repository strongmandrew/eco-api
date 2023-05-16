package com.example.domain.usecase.recyclePoint

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.domain.dao.RubbishTypeDao
import com.example.utils.ServiceResult

class AddAcceptedRubbishType(
    private val recyclePointDao: RecyclePointDao,
    private val rubbishTypeDao: RubbishTypeDao
) {
    suspend operator fun invoke(idRecyclePoint: Int, name: String):
            Response<Boolean> {
        return when (val foundRubbishType = rubbishTypeDao
            .getRubbishTypeByName(name)) {

            is ServiceResult.Success -> {

                when (val insert = recyclePointDao.addAcceptedRubbishType(
                    idRecyclePoint, foundRubbishType.data.id!!
                )) {
                    is ServiceResult.Success -> Response(
                        data = insert.data,
                        statusCode = 200
                    )

                    is ServiceResult.Error -> Response(
                        error = ErrorResponse(
                            insert.error.name, insert.error.message
                        ),
                        statusCode = insert.error.statusCode
                    )
                }
            }

            is ServiceResult.Error -> Response(
                error = ErrorResponse(
                    foundRubbishType.error.name, foundRubbishType
                        .error.message
                ),
                statusCode = foundRubbishType.error.statusCode
            )
        }
    }
}