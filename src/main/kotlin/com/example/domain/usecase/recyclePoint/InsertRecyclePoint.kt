package com.example.domain.usecase.recyclePoint

import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

class InsertRecyclePoint(
    private val recyclePointDao: RecyclePointDao,
    private val noPointNearby: NoPointNearby
) {

    suspend operator fun invoke(recyclePoint: RecyclePoint): Response<RecyclePoint> {

        return when (val result = noPointNearby(recyclePoint)) {

            is ServiceResult.Success -> {

                when (val insertPoint = recyclePointDao.registerPoint(recyclePoint)) {
                    is ServiceResult.Success -> {
                        Response(
                            data = insertPoint.data
                        )
                    }
                    is ServiceResult.Error -> {

                        Response(
                            errors = insertPoint.error
                        )
                    }
                }
            }
            is ServiceResult.Error -> {
                Response(
                    errors = result.error
                )
            }
        }

    }
}