package com.example.domain.usecase.review

import com.example.domain.Response
import com.example.domain.dao.ReviewDao
import com.example.entity.Review
import com.example.utils.ServiceResult

class GetReviewsByPoint(
    private val reviewDao: ReviewDao
) {

    suspend operator fun invoke(idPoint: Int): Response<List<Review>> {

        return when (val result = reviewDao.getReviewsByPointId(idPoint)){
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