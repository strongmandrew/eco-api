package com.example.domain.usecase.recyclePoint

import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

class GetRecyclePointById(
    private val recyclePointDao: RecyclePointDao
) {

    suspend operator fun invoke(idPoint: Int): Response<RecyclePoint> {

        return when (val result = recyclePointDao.getPointById(idPoint)) {
            is ServiceResult.Success -> {
                Response(
                    data = result.data
                )

            }
            is ServiceResult.Error -> {
                Response(
                    errors = result.error
                )
            }
        }
    }
}