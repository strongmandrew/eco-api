package com.example.domain.usecase.recyclePoint

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

class GetPointsFilteredByType(
    private val recyclePointDao: RecyclePointDao
) {

    suspend operator fun invoke(type: String):
            Response<List<RecyclePoint>> {
        val splitTypes = type.split(",")
        return when (val points = recyclePointDao
            .getPointsFilteredByType(splitTypes)) {

            is ServiceResult.Error -> Response(
                error = ErrorResponse(
                    points.error.name, points.error.message
                ),
                statusCode = points.error.statusCode
            )
            is ServiceResult.Success -> Response(
                data = points.data,
                statusCode = 200
            )
        }
    }
}