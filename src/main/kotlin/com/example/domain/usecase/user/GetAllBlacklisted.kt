package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.BlacklistDao
import com.example.utils.ServiceResult

class GetAllBlacklisted(
    private val blacklistDao: BlacklistDao
) {
    suspend operator fun invoke(): Response<List<String>> {
        return when (val list = blacklistDao.getAllBlacklisted()) {

            is ServiceResult.Success -> Response(
                data = list.data,
                statusCode = 200
            )

            is ServiceResult.Error -> Response(
                error = ErrorResponse(
                    list.error.name, list.error.message
                ),
                statusCode = list.error.statusCode
            )
        }
    }
}