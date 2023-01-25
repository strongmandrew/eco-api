package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.domain.dao.ReviewDao
import com.example.entity.Review
import com.example.utils.Errors
import com.example.utils.ServiceResult

class ReviewDaoImpl: ReviewDao {

    override suspend fun getReviewsByPointId(id: Int): ServiceResult<List<Review>> {
        TODO("Not yet implemented")
    }

    override suspend fun postReview(review: Review): ServiceResult<Review> {
        TODO("Not yet implemented")
    }
}