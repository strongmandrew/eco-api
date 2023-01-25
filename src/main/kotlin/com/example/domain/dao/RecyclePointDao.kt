package com.example.domain.dao

import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

interface RecyclePointDao {
    suspend fun getPoints(): ServiceResult<List<RecyclePoint>>
    suspend fun getPointById(id: Int): ServiceResult<RecyclePoint>
    suspend fun suchPointDoesNotExist(recyclePoint: RecyclePoint): ServiceResult<Boolean>
    suspend fun uploadMultipartPhoto(photoBytes: ByteArray, photoName: String): ServiceResult<Boolean>
    suspend fun registerPoint(point: RecyclePoint): ServiceResult<Int>
    suspend fun changePointApprovalById(idPoint: Int, approval: Boolean): ServiceResult<Boolean>
    suspend fun getPointApprovalById(idPoint: Int): ServiceResult<Boolean>
    suspend fun deletePoint(idPoint: Int): ServiceResult<Boolean>
}