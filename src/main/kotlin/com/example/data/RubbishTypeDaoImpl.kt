package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.data.database.RubbishTypeTable
import com.example.data.database.UserTakeOffTable
import com.example.domain.dao.RubbishTypeDao
import com.example.entity.RubbishType
import com.example.utils.Errors
import com.example.utils.ServiceResult
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.sum

class RubbishTypeDaoImpl: RubbishTypeDao {
    override suspend fun insertRubbishType(rubbishType: RubbishType): ServiceResult<RubbishType> {
        return try {
            dbQuery {
                RubbishTypeTable.insert {
                    it[this.type] = rubbishType.type
                    it[this.description] = rubbishType.description
                }.resultedValues?.singleOrNull()?.let {
                    ServiceResult.Success(rowToRubbishType(it))
                } ?: ServiceResult.Error(Errors.INSERT_FAILED)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getRubbishTypeById(rubbishTypeId: Int): ServiceResult<RubbishType> {
        return try {
            RubbishTypeTable.select {
                RubbishTypeTable.id eq rubbishTypeId
            }.singleOrNull()?.let {
                    ServiceResult.Success(rowToRubbishType(it))
                } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getRubbishTypeByName(rubbishType: String): ServiceResult<RubbishType> {
        return try {
            dbQuery {
                RubbishTypeTable.select {
                    RubbishTypeTable.type eq rubbishType
                }.singleOrNull()?.let {
                    ServiceResult.Success(rowToRubbishType(it))
                } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getTotalRubbishTypeTakeOffById
                (rubbishTypeId: Int): ServiceResult<Double> {
        return try {
            dbQuery {
                UserTakeOffTable.slice(UserTakeOffTable
                    .amountInGrams.sum())
                    .select { UserTakeOffTable.idRubbishType eq rubbishTypeId }
                    .singleOrNull()?.let {
                        ServiceResult.Success(it[UserTakeOffTable.amountInGrams])
                    } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    private fun rowToRubbishType(row: ResultRow): RubbishType {
        return RubbishType(
            id = row[RubbishTypeTable.id].value,
            type = row[RubbishTypeTable.type],
            description = row[RubbishTypeTable.description]
        )
    }
}