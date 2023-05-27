package com.example.domain.dao

import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult
import io.ktor.utils.io.*

interface RecyclePointDao {
    suspend fun getPoints(): ServiceResult<List<RecyclePoint>>
    suspend fun getPointById(id: Int): ServiceResult<RecyclePoint>
    suspend fun getPointByQuery(query: String):
            ServiceResult<List<RecyclePoint>>
    suspend fun suchPointDoesNotExist(recyclePoint: RecyclePoint): ServiceResult<Boolean>
    suspend fun uploadChannelPhoto(photoChannel: ByteReadChannel, photoName: String):
            ServiceResult<Boolean>
    suspend fun registerPoint(point: RecyclePoint): ServiceResult<RecyclePoint>
    suspend fun changePointApprovalById(idPoint: Int, approval: Boolean): ServiceResult<Boolean>
    suspend fun getPointApprovalById(idPoint: Int): ServiceResult<Boolean>
    suspend fun deletePoint(idPoint: Int): ServiceResult<Boolean>
    suspend fun insertPhotoPath(idPoint: Int, photoPath: String): ServiceResult<Boolean>
    suspend fun getPointsFilteredByType(types: List<String>):
            ServiceResult<List<RecyclePoint>>
    suspend fun addAcceptedRubbishType(
        idRecyclePoint: Int, type: Int
    ): ServiceResult<Boolean>
}