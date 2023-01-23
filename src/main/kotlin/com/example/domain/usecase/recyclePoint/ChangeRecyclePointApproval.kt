package com.example.domain.usecase.recyclePoint

import com.example.domain.Response
import com.example.domain.dao.RecyclePointDao
import com.example.utils.ServiceResult

class ChangeRecyclePointApproval (
    private val recyclePointDao: RecyclePointDao
) {

    suspend operator fun invoke(idPoint: Int): Response<Boolean> {

        return when (val approve = recyclePointDao.changePointApprovalById(idPoint)) {

            is ServiceResult.Success -> {
                Response(
                    data = approve.data
                )
            }
            is ServiceResult.Error -> {
                Response(
                    errors = approve.error
                )
            }
        }

    }

}