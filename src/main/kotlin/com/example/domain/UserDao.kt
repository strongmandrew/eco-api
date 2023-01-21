package com.example.domain

import com.example.entity.User
import com.example.utils.ServiceResult

interface UserDao {
    suspend fun getUserByEmail(email: String): ServiceResult<User>
    suspend fun registerUser(user: User): ServiceResult<User>
}