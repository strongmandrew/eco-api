package com.example.domain.usecase.recyclePoint

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.plugins.recyclePointModule
import com.example.utils.ServiceResult

class SetRecyclePointPhoto(
    private val recyclePointDao: RecyclePointDao
) {

    suspend operator fun invoke(photoBytes: ByteArray, idPoint: Int): Response<Boolean> {

        return when (val point = recyclePointDao.getPointById(idPoint)) {

            is ServiceResult.Success -> {
                val photoName = "${point.data.streetName}&${point.data.streetHouseNum}&${point
                    .data.latitude}&${point.data.longitude}"

                when (val photo = recyclePointDao.uploadMultipartPhoto(photoBytes, photoName)) {

                    is ServiceResult.Success -> {
                        Response(
                            data = photo.data
                        )
                    }
                    is ServiceResult.Error -> {
                        Response(
                            error = ErrorResponse(photo.error.name, photo.error.message)
                        )
                    }
                }
            }

            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(point.error.name, point.error.message)
                )
            }

        }



    }

}