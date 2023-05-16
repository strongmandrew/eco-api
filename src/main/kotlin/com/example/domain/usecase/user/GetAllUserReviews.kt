package com.example.domain.usecase.user

import com.example.domain.ErrorResponse
import com.example.domain.Response
import com.example.domain.dao.ReviewDao
import com.example.entity.Review
import com.example.utils.ServiceResult

class GetAllUserReviews(
    private val reviewDao: ReviewDao
) {

    suspend operator fun invoke(idUser: Int):
            Response<List<Review>> {
        return when (val reviews = reviewDao.getAllUserReviews
            (idUser)) {

            is ServiceResult.Success -> Response(
                data = reviews.data,
                statusCode = 200
            )
            is ServiceResult.Error -> Response(
                error = ErrorResponse(
                    reviews.error.name, reviews.error.message
                ),
                statusCode = reviews.error.statusCode
            )
        }
    }
}