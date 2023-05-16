package com.example.domain.dao

import com.example.entity.Review
import com.example.utils.ServiceResult

interface ReviewDao {
    suspend fun getReviewsByPointId(id: Int): ServiceResult<List<Review>>
    suspend fun postReview(review: Review, idPoint: Int, idUser: Int):
            ServiceResult<Review>
    suspend fun deleteReviewById(idReview: Int): ServiceResult<Boolean>
    suspend fun getReviewById(idReview: Int): ServiceResult<Review>
}