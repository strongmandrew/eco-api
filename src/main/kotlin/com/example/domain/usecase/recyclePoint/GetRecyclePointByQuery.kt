package com.example.domain.usecase.recyclePoint

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

class GetRecyclePointByQuery(
    private val recyclePointDao: RecyclePointDao
) {
    suspend operator fun invoke(query: String):
            Response<List<RecyclePoint>> {
        return when (val point = recyclePointDao.getPointByQuery
            (query)) {

            is ServiceResult.Success -> {
                Response(
                    data = point.data,
                    statusCode = 200
                )
            }
            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        point.error.name, point.error.message
                    ),
                    statusCode = point.error.statusCode
                )
            }

        }
    }
}