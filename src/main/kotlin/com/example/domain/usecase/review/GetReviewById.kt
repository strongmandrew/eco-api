package com.example.domain.usecase.review

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.ReviewDao
import com.example.entity.Review
import com.example.utils.ServiceResult

class GetReviewById(
    private val reviewDao: ReviewDao
) {

    suspend operator fun invoke(idReview: Int): Response<Review> {

        return when (val review = reviewDao.getReviewById(idReview)) {
            is ServiceResult.Success -> {
                Response(
                    data = review.data,
                    statusCode = 200
                )
            }
            is ServiceResult.Error -> {
                Response(
                    error = ErrorResponse(review.error.name, review.error.message),
                    statusCode = review.error.statusCode
                )
            }
        }
    }
}