package com.example.domain.usecase.recyclePoint

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.ServiceResult

class InsertRecyclePoint(
    private val recyclePointDao: RecyclePointDao,
) {


    suspend operator fun invoke(recyclePoint: RecyclePoint):
            Response<RecyclePoint> {


        return when (val result = recyclePointDao.suchPointDoesNotExist(recyclePoint)) {

            is ServiceResult.Success -> {


                when (val insert = recyclePointDao.registerPoint(recyclePoint)) {

                    is ServiceResult.Success -> {

                        Response(
                            data = recyclePoint,
                            statusCode = 201
                        )

                    }
                    is ServiceResult.Error -> {
                        Response(
                            statusCode = insert.error.statusCode,
                            error = ErrorResponse(insert.error.name, insert.error.message)
                        )
                    }
                }

            }
            is ServiceResult.Error -> {
                Response(
                    statusCode = result.error.statusCode,
                    error = ErrorResponse(result.error.name, result.error.message)
                )
            }
        }
    }





}