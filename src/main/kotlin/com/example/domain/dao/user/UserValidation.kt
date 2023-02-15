package com.example.domain.dao.user

import com.example.utils.ServiceResult

interface UserValidation {

    suspend fun setValidationCode(email: String, code: Int):
            ServiceResult<Boolean>
    suspend fun compareValidationCode(email: String, code: Int):
            ServiceResult<Boolean>

    suspend fun isCodeNotBlank(email: String): ServiceResult<Boolean>
}