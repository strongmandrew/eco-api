package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.BlacklistDao
import com.example.utils.ServiceResult

class RemoveFromBlacklist(
    private val blacklistDao: BlacklistDao
) {
    suspend operator fun invoke(email: String): Response<Boolean> {
        return when (
            val unban = blacklistDao.removeFromBlacklist(email)
        ) {

            is ServiceResult.Success -> Response(
                data = unban.data,
                statusCode = 200
            )
            is ServiceResult.Error -> Response(
                error = ErrorResponse(
                    unban.error.name, unban.error.message
                ),
                statusCode = unban.error.statusCode
            )
        }
    }
}