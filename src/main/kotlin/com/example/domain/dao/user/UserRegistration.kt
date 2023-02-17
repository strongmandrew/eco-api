package com.example.domain.dao.user

import com.example.entity.User
import com.example.utils.ServiceResult

interface UserRegistration {

    suspend fun registerUser(user: User): ServiceResult<User>
    suspend fun isEmailNotInBlacklist(email: String):
            ServiceResult<Boolean>

}