package com.example.domain.usecase.review

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.ReviewDao
import com.example.entity.Review
import com.example.utils.ServiceResult

class GetReviewsByPointId(
    private val reviewDao: ReviewDao
) {

    suspend operator fun invoke(idPoint: Int): Response<List<Review>> {

        return when (val result = reviewDao.getReviewsByPointId(idPoint)){
            is ServiceResult.Success -> {
                Response(
                    data = result.data,
                    statusCode = 200
                )
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