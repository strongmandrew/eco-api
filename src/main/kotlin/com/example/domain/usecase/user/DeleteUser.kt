package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserDao
import com.example.utils.ServiceResult

class DeleteUser(
    private val userDao: UserDao,
) {
    suspend operator fun invoke(idUser: Int): Response<Boolean> {

        return when (val delete = userDao.deleteUser(idUser)) {
            is ServiceResult.Success -> Response(
                data = delete.data,
                statusCode = 200
            )

            is ServiceResult.Error -> Response(
                error = ErrorResponse(
                    delete.error.name, delete
                        .error.message
                ),
                statusCode = delete.error.statusCode
            )
        }
    }
}