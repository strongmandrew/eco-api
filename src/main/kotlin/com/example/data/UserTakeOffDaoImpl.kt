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
    ): ServiceResult<UserTakeOff> {
        return try {
            dbQuery {
                UserTakeOffTable.insert {
                    it[this.idUser] = takeOff.idUser
                    it[this.idRecyclePoint] = takeOff.idRecyclePoint
                    it[this.idRubbishType] = takeOff.idRecyclePoint
                    it[this.amountInGrams] = takeOff.amountInGrams
                }.resultedValues?.singleOrNull()?.let {
                    ServiceResult.Success(rowToUserTakeOff(it))
                } ?: ServiceResult.Error(Errors.INSERT_FAILED)
            }
        } catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun totalUserTakeOff(idUser: Int): ServiceResult<Double> {
        return try {
            dbQuery {
                UserTakeOffTable
                    .slice(UserTakeOffTable.amountInGrams.sum())
                    .select { UserTakeOffTable.idUser eq idUser }
                    .singleOrNull()?.let {
                        ServiceResult.Success(it[UserTakeOffTable.amountInGrams])
                    } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        } catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getUserTakeOffById(idUser: Int): ServiceResult<List<UserTakeOff>> {
        return try {
            dbQuery {
                UserTakeOffTable
                    .select { UserTakeOffTable.idUser eq idUser }
                    .map {
                        rowToUserTakeOff(it)
                    }
                    .let { ServiceResult.Success(it) }
            }
        } catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getTakeOffById(id: Int): ServiceResult<UserTakeOff> {
        return try {
            dbQuery {
                UserTakeOffTable
                    .select { UserTakeOffTable.id eq id }
                    .singleOrNull()?.let {
                        ServiceResult.Success(rowToUserTakeOff(it))
                    } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        } catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun revertTakeOff(idTakeOff: Int): ServiceResult<Boolean> {
        return try {
            dbQuery {
                if (UserTakeOffTable
                    .deleteWhere { UserTakeOffTable.id eq idTakeOff} > 0
                    )
                    ServiceResult.Success(true)

                else ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    private fun rowToUserTakeOff(row: ResultRow): UserTakeOff {
        return UserTakeOff(
            id = row[UserTakeOffTable.id].value,
            idUser = row[UserTakeOffTable.idUser].value,
            idRecyclePoint = row[UserTakeOffTable.idRecyclePoint]
                .value,
            idRubbishType = row[UserTakeOffTable.idRubbishType].value,
            amountInGrams = row[UserTakeOffTable.amountInGrams]
        )
    }
}