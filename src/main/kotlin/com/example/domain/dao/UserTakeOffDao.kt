package com.example.domain.dao

import com.example.entity.UserTakeOff
import com.example.utils.ServiceResult

interface UserTakeOffDao {
    suspend fun takeOffRubbish(takeOff: UserTakeOff):
            ServiceResult<UserTakeOff>
    suspend fun totalUserTakeOff(idUser: Int): ServiceResult<Double>
    suspend fun getUserTakeOffById(idUser: Int):
            ServiceResult<List<UserTakeOff>>
    suspend fun getTakeOffById(id: Int): ServiceResult<UserTakeOff>
    suspend fun revertTakeOff(idTakeOff: Int): ServiceResult<Boolean>
}