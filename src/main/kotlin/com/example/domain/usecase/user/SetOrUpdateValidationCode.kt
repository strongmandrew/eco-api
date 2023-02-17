package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.UserDao
import com.example.utils.ServiceResult

class SetOrUpdateValidationCode(

    private val userDao: UserDao
) {

    suspend operator fun invoke(email: String, code: Int):
            ServiceResult<Boolean> {

        return when (val entry = userDao.noEntriesOfEmail(email)) {

            is ServiceResult.Success -> {

                if (entry.data) {

                    when (val set = userDao.setValidationCode
                        (email, code)) {

                        is ServiceResult.Success -> {

                            ServiceResult.Success(set.data)
                        }

                        is ServiceResult.Error -> {

                            ServiceResult.Error(set.error)
                        }
                    }

                } else {

                    when (val update = userDao.updateValidationCode
                        (email, code)) {

                        is ServiceResult.Success -> {
                            ServiceResult.Success(update.data)
                        }

                        is ServiceResult.Error -> {

                            ServiceResult.Error(update.error)
                        }
                    }
                }

            }

            is ServiceResult.Error -> {
                ServiceResult.Error(entry.error)
            }
        }
    }
}