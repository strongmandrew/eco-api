package com.example.domain.usecase.review

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.ReviewDao
import com.example.utils.ServiceResult

class DeleteReviewById(
    private val reviewDao: ReviewDao
) {

    suspend operator fun invoke(idReview: Int): Response<Boolean> {

        return when (val delete = reviewDao.deleteReviewById(idReview)) {

            is ServiceResult.Success -> {
                Response(
                    data = delete.data,
                    statusCode = 200
                )
            }
            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(delete.error.name, delete.error.message),
                    statusCode = delete.error.statusCode
                )
            }
        }

    }
}