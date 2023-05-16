package com.example.domain.dao

import com.example.entity.RubbishType
import com.example.utils.ServiceResult

interface RubbishTypeDao {

    suspend fun insertRubbishType(rubbishType: RubbishType): ServiceResult<RubbishType>
    suspend fun getRubbishTypeById(rubbishTypeId: Int): ServiceResult<RubbishType>

    suspend fun getRubbishTypeByName(name: String): ServiceResult<RubbishType>
    suspend fun getTotalRubbishTypeTakeOffByName(rubbishType: String):
            ServiceResult<Double>
    suspend fun deleteRubbishTypeById(rubbishTypeId: Int):
            ServiceResult<Boolean>
}