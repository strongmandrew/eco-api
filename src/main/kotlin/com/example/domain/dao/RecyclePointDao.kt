package com.example.domain.dao

import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

interface RecyclePointDao {
    suspend fun getPoints(): ServiceResult<List<RecyclePoint>>
    suspend fun getPointById(id: Int): ServiceResult<RecyclePoint>
    suspend fun nearPointDoesNotExist(recyclePoint: RecyclePoint): ServiceResult<RecyclePoint>
    suspend fun registerPoint(point: RecyclePoint): ServiceResult<RecyclePoint>
    suspend fun changePointApprovalById(id: Int): ServiceResult<Boolean>
}