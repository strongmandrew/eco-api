package com.example.domain

import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

interface RecyclePointDao {
    suspend fun getPoints(): ServiceResult<List<RecyclePoint>>
    suspend fun getPointById(id: Int): ServiceResult<RecyclePoint>
    suspend fun registerPoint(point: RecyclePoint): ServiceResult<RecyclePoint>
    suspend fun approvePointById(id: Int): ServiceResult<Boolean>
}