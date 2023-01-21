package com.example.domain

import com.example.entity.RubbishType
import com.example.utils.ServiceResult

interface RubbishTypeDao {
    suspend fun postRubbishType(rubbishType: RubbishType): ServiceResult<Boolean>
}