package com.example.domain.usecase.recyclePoint

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.utils.ServiceResult
import io.ktor.utils.io.*

class SetRecyclePointPhoto(
    private val recyclePointDao: RecyclePointDao,
    private val recyclePointFileNameGenerator: RecyclePointFileNameGenerator
) {

    suspend operator fun invoke(photoChannel: ByteReadChannel, idPoint: Int): Response<Boolean> {

        return when (val point = recyclePointDao.getPointById(idPoint)) {

            is ServiceResult.Success -> {

                val photoName = recyclePointFileNameGenerator(point.data)

                when (val photo = recyclePointDao.uploadChannelPhoto(photoChannel, photoName)) {

                    is ServiceResult.Success -> {

                        when (val entry = recyclePointDao.insertPhotoPath(idPoint, photoName)) {

                            is ServiceResult.Success -> {
                                Response(
                                    data = entry.data,
                                    statusCode = 201
                                )
                            }
                            is ServiceResult.Error -> {
                                Response(
                                    statusCode = entry.error.statusCode,
                                    error = ErrorResponse(entry.error.name, entry.error.message)
                                    )

                            }
                        }
                    }
                    is ServiceResult.Error -> {
                        Response(
                            statusCode = photo.error.statusCode,
                            error = ErrorResponse(photo.error.name, photo.error.message)
                        )
                    }
                }
            }

            is ServiceResult.Error -> {
                Response(
                    statusCode = point.error.statusCode,
                    error = ErrorResponse(point.error.name, point.error.message)
                )
            }

        }



    }

}