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

class ReviewDaoImpl : ReviewDao {

    override suspend fun getReviewsByPointId(id: Int) = try {
        val points = dbQuery {
            ReviewTable.join(
                otherTable = RecyclePointTable,
                joinType = JoinType.INNER,
                onColumn = ReviewTable.recyclePoint,
                otherColumn = RecyclePointTable.id
            ).slice(
                ReviewTable.id,
                ReviewTable.review,
                ReviewTable.dateCreated
            )
                .select { ReviewTable.recyclePoint eq id }
                .toList()
                .map { it.toReview() }
        }

        ServiceResult.Success(points)
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun getReviewById(idReview: Int) = try {
        dbQuery {
            ReviewTable.select { ReviewTable.id eq idReview }.toList()
                .singleOrNull()?.let {
                    ServiceResult.Success(it.toReview())
                } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }


    override suspend fun deleteReviewById(idReview: Int) = try {
        dbQuery {
            val deleteSucceeded = ReviewTable.deleteWhere {
                ReviewTable.id eq idReview
            } > 0

            ServiceResult.Success(deleteSucceeded)
        }

    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun postReview(
        review: Review,
        idPoint: Int,
        idUser: Int
    ) = try {
        dbQuery {
            ReviewTable.insert {
                it[ReviewTable.review] = review.reviewText
                it[recyclePoint] = idPoint
                it[ReviewTable.idUser] = idUser
            }.resultedValues?.singleOrNull()?.let {
                ServiceResult.Success(it.toReview())
            } ?: ServiceResult.Error(Errors.EMPTY_DATA)
        }
    } catch (e: Exception) {
        println(e.message)
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    private fun ResultRow.toReview() = Review(
        id = this[ReviewTable.id].value,
        reviewText = this[ReviewTable.review],
        dateOf = this[ReviewTable.dateCreated].toString(),
    )
}