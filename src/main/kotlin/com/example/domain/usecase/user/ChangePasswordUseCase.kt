package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.INCORRECTPASSWORDResponse
import com.example.domain.PASSWORDSMISMATCHResponse
import com.example.domain.Response
import com.example.domain.dao.UserDao
import com.example.domain.usecase.user.jwt.JWTHandler
import com.example.entity.ChangePassword
import com.example.utils.ServiceResult

class ChangePasswordUseCase(
    private val userDao: UserDao,
    private val jwtHandler: JWTHandler,
    private val passwordEncrypt: PasswordEncrypt,
) {

    suspend operator fun invoke(
        idUser: Int,
        changePassword: ChangePassword,
    ): Response<String> {
        if (changePassword.newPassword1 != changePassword
                .newPassword2
        ) return PASSWORDSMISMATCHResponse

        return when (val user = userDao.getUserById(idUser)) {

            is ServiceResult.Success -> {

                changePassword.previousPassword?.let {

                    if (passwordEncrypt(it) != user.data.password)
                        INCORRECTPASSWORDResponse
                    else
                        changePassword(
                            idUser,
                            changePassword.newPassword1
                        )
                } ?: changePassword(
                    idUser,
                    changePassword.newPassword1
                )
            }

            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(
                        user.error.name, user
                            .error.message
                    ),
                    statusCode = user.error.statusCode
                )
            }
        }
    }

    private suspend fun changePassword(
        idUser: Int,
        password:
        String,
    ): Response<String> {
        return when (val change = userDao.changeUserPasswordById(
            idUser,
            passwordEncrypt(password)
        )) {

            is ServiceResult.Success -> {

                when (val user = userDao.getUserById(idUser)) {

                    is ServiceResult.Success -> {

                        when (val updateChanged = userDao
                            .incrementTimesChanged(
                                idUser, user
                                    .data.timesChanged!!
                            )) {

                            is ServiceResult.Success -> {
                                val receivedUser = user.data
                                Response(
                                    data = jwtHandler.generateToken(
                                        receivedUser.id!!,
                                        receivedUser.role!!,
                                        receivedUser.timesChanged!!
                                    ),
                                    statusCode = 201
                                )
                            }

                            is ServiceResult.Error ->
                                Response(
                                    error = ErrorResponse(
                                        updateChanged.error.name,
                                        updateChanged.error.message
                                    ),
                                    statusCode = updateChanged.error.statusCode
                                )
                        }
                    }

                    is ServiceResult.Error -> Response(
                        error = ErrorResponse(
                            user.error.name, user
                                .error.message
                        ),
                        statusCode = user.error.statusCode
                    )
                }
            }

            is ServiceResult.Error -> Response(
                error = ErrorResponse(
                    change.error.name, change.error
                        .message
                ),
                statusCode = change.error.statusCode
            )
        }
    }
}