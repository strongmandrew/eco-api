package com.example.domain.usecase.recyclePoint

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.utils.ServiceResult
import io.ktor.utils.io.*

class DownloadPointPhoto(
    private val recyclePointDao: RecyclePointDao
) {

    suspend operator fun invoke(idPoint: Int): Response<ByteReadChannel> {

        return when (val result = recyclePointDao.downloadPointPhoto(idPoint)) {

            is ServiceResult.Success -> {
                Response(
                    statusCode = 200,
                    data = result.data
                )
            }
            is ServiceResult.Error -> {
                Response(
                    statusCode = result.error.statusCode,
                    error = ErrorResponse(
                        result.error.name, result.error.message
                    )
                )
            }
        }
    }
}