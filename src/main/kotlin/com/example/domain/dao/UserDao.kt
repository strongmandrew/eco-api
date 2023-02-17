package com.example.domain.dao

import com.example.domain.dao.user.UserRegistration
import com.example.domain.dao.user.UserValidation
import com.example.entity.User
import com.example.utils.ServiceResult

interface UserDao: UserRegistration, UserValidation {

    suspend fun getUserByEmail(email: String): ServiceResult<User>
    suspend fun emailDoesNotExist(email: String): ServiceResult<Boolean>

}