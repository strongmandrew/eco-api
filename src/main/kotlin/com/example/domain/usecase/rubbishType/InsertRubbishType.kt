package com.example.domain.usecase.rubbishType

import com.example.domain.Response
import com.example.domain.dao.RubbishTypeDao
import com.example.entity.RubbishType
import com.example.utils.ServiceResult

class InsertRubbishType(
    private val rubbishTypeDao: RubbishTypeDao
) {

    suspend operator fun invoke(rubbishType: RubbishType): Response<RubbishType> {

        return when (val result = rubbishTypeDao.insertRubbishType(rubbishType)) {
            is ServiceResult.Success -> {
                Response(
                    data = result.data
                )
            }
            is ServiceResult.Error -> {
                Response(
                    errors = result.error
                )
            }
        }
    }
}