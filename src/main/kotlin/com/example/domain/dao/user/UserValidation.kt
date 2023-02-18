package com.example.domain.dao.user

import com.example.utils.ServiceResult

interface UserValidation {

    suspend fun setValidationCode(email: String, code: Int):
            ServiceResult<Boolean>
    suspend fun updateValidationCode(email: String, code: Int):
            ServiceResult<Boolean>
    suspend fun compareValidationCode(email: String, code: Int):
            ServiceResult<Boolean>
    suspend fun noEntriesOfEmail(email: String):
            ServiceResult<Boolean>
    suspend fun approveUserEmail(email: String): ServiceResult<Boolean>
}