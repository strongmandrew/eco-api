package com.example.domain.usecase.recyclePoint

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

class GetRecyclePoints(
    private val recyclePointDao: RecyclePointDao
) {

    suspend operator fun invoke(): Response<List<RecyclePoint>> {

        return when (val points = recyclePointDao.getPoints()) {
            is ServiceResult.Success -> {
                Response(
                    data = points.data,
                    statusCode = 200
                )
            }
            is ServiceResult.Error -> {
                Response(
                    statusCode = points.error.statusCode,
                    error = ErrorResponse(points.error.name, points.error.message)
                )
            }
        }
    }
}