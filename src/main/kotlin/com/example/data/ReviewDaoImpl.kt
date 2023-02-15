package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.data.database.RecyclePointTable
import com.example.data.database.ReviewTable
import com.example.domain.dao.ReviewDao
import com.example.entity.Review
import com.example.utils.Errors
import com.example.utils.ServiceResult
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ReviewDaoImpl: ReviewDao {

    override suspend fun getReviewsByPointId(id: Int): ServiceResult<List<Review>> {
        return try {
            val points = dbQuery {
                ReviewTable.join(
                    otherTable = RecyclePointTable,
                    joinType = JoinType.INNER,
                    onColumn = ReviewTable.recyclePoint,
                    otherColumn = RecyclePointTable.id
                ).slice(
                    ReviewTable.id, ReviewTable.review, ReviewTable.dateCreated, ReviewTable.rating
                ).select { ReviewTable.recyclePoint eq id }.toList().map { rowToReview(it) }
            }

            if (points.isNotEmpty()) ServiceResult.Success(points)
            else ServiceResult.Error(Errors.EMPTY_DATA)
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getReviewById(idReview: Int): ServiceResult<Review> {
        return try {
            dbQuery {
                ReviewTable.select { ReviewTable.id eq idReview }.toList().singleOrNull()?.let {
                    ServiceResult.Success(rowToReview(it))
                } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun deleteReviewById(idReview: Int): ServiceResult<Boolean> {
        return try {
            dbQuery {
                if (ReviewTable.deleteWhere { ReviewTable.id eq idReview } > 0)
                    ServiceResult.Success(true)
                else
                    ServiceResult.Error(Errors.ID_NOT_FOUND)
            }

        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun postReview(review: Review, idPoint: Int): ServiceResult<Review> {
        return try {
            dbQuery {
                ReviewTable.insert {
                    it[ReviewTable.review] = review.reviewText
                    it[rating] = review.rating
                    it[recyclePoint] = idPoint
                }.resultedValues?.singleOrNull()?.let {
                    ServiceResult.Success(rowToReview(it))
                } ?: ServiceResult.Error(Errors.EMPTY_DATA)
            }
        }
        catch (e: Exception) {
            println(e.message)
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    private fun rowToReview(row: ResultRow): Review {
        return Review(
            id = row[ReviewTable.id].value,
            reviewText = row[ReviewTable.review],
            dateOf = row[ReviewTable.dateCreated].toString(),
            rating = row[ReviewTable.rating],
        )
    }
}