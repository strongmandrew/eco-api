package com.example.domain.dao

import com.example.utils.ServiceResult

interface BlacklistDao {

    suspend fun getAllBlacklisted(): ServiceResult<List<String>>

    suspend fun blackListEmail(email: String): ServiceResult<Boolean>
    suspend fun removeFromBlacklist(email: String): ServiceResult<Boolean>
}