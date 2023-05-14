package com.example.domain.dao.user

import com.example.utils.ServiceResult

interface UserProfile {

    suspend fun changeUserPasswordById(idUser: Int, password: String):
            ServiceResult<Boolean>
}