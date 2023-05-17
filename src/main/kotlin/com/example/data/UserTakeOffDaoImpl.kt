package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.data.database.UserTakeOffTable
import com.example.domain.dao.UserTakeOffDao
import com.example.entity.UserTakeOff
import com.example.utils.Errors
import com.example.utils.ServiceResult
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserTakeOffDaoImpl : UserTakeOffDao {
    override suspend fun takeOffRubbish(
        takeOff: UserTakeOff,
    ) = try {
        dbQuery {
            UserTakeOffTable.insert {
                it[this.idUser] = takeOff.idUser!!
                it[this.idRecyclePoint] = takeOff.idRecyclePoint
                it[this.idRubbishType] = takeOff.idRubbishType
                it[this.amountInGrams] = takeOff.amountInGrams
                it[this.percentRating] = takeOff.percentRating
            }.resultedValues?.singleOrNull()?.let {
                ServiceResult.Success(it.toUserTakeoff())
            } ?: ServiceResult.Error(Errors.INSERT_FAILED)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun totalUserTakeOff(idUser: Int) = try {
        dbQuery {
            UserTakeOffTable
                .slice(UserTakeOffTable.amountInGrams.sum())
                .select { UserTakeOffTable.idUser eq idUser }
                .singleOrNull()?.let {
                    ServiceResult.Success(
                        it[UserTakeOffTable
                            .amountInGrams.sum()] ?: 0.0
                    )
                } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        println(e.message)
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun getUserTakeOffById(idUser: Int) = try {
        dbQuery {
            UserTakeOffTable
                .select { UserTakeOffTable.idUser eq idUser }
                .map {
                    it.toUserTakeoff()
                }
                .let { ServiceResult.Success(it) }
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun getTakeOffById(
        id: Int,
    ) = try {
        dbQuery {
            UserTakeOffTable
                .select { UserTakeOffTable.id eq id }
                .singleOrNull()?.let {
                    ServiceResult.Success(it.toUserTakeoff())
                } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun revertTakeOff(idTakeOff: Int): ServiceResult<Boolean> {
        return try {
            dbQuery {
                if (UserTakeOffTable
                        .deleteWhere { UserTakeOffTable.id eq idTakeOff } > 0
                )
                    ServiceResult.Success(true)
                else ServiceResult.Error(Errors.NOT_FOUND)
            }
        } catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    private fun ResultRow.toUserTakeoff(): UserTakeOff {
        return UserTakeOff(
            id = this[UserTakeOffTable.id].value,
            idUser = this[UserTakeOffTable.idUser].value,
            idRecyclePoint = this[UserTakeOffTable.idRecyclePoint]
                .value,
            idRubbishType = this[UserTakeOffTable.idRubbishType].value,
            amountInGrams = this[UserTakeOffTable.amountInGrams],
            datetime = this[UserTakeOffTable.datetime].toString(),
            percentRating = this[UserTakeOffTable.percentRating],
        )
    }
}