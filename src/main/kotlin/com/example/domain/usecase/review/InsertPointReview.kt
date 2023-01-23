package com.example.domain.usecase.review

import com.example.domain.Response
import com.example.domain.dao.ReviewDao
import com.example.entity.Review
import com.example.utils.ServiceResult

class InsertPointReview(
    private val reviewDao: ReviewDao
) {

    suspend operator fun invoke(review: Review): Response<Review> {

        return when (val result = reviewDao.postReview(review)) {

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