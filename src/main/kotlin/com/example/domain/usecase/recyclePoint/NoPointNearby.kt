package com.example.domain.usecase.recyclePoint

import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

class NoPointNearby(
    private val recyclePointDao: RecyclePointDao
) {

    companion object {
        const val LATITUDE_FAULT = 2.0
        const val LONGITUDE_FAULT = 2.0
    }

    suspend operator fun invoke(point: RecyclePoint): ServiceResult<Boolean> {

        return when (val result = recyclePointDao.nearPointDoesNotExist(point)) {

            is ServiceResult.Success -> {
                ServiceResult.Success(true)
            }
            is ServiceResult.Error -> {
                ServiceResult.Error(
                    listOf(result.error.first())
                )
            }
        }

    }
}