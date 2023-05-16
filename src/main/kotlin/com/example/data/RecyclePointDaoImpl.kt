package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.data.database.RecyclePointRubbishTypeTable
import com.example.data.database.RecyclePointTable
import com.example.data.database.RecyclePointTypeTable
import com.example.data.database.RubbishTypeTable
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.Const
import com.example.utils.Errors
import com.example.utils.ServiceResult
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.io.File

class RecyclePointDaoImpl : RecyclePointDao {

    override suspend fun getPoints():
            ServiceResult<List<RecyclePoint>> = try {
        val points = dbQuery {
            RecyclePointTable.join(
                otherTable = RecyclePointTypeTable,
                joinType = JoinType.INNER,
                onColumn = RecyclePointTable.type,
                otherColumn = RecyclePointTypeTable.id
            ).slice(pointsFieldSet)
                .selectAll()
                .toList()
                .map {
                    it.toRecyclePoint()
                }
        }
        ServiceResult.Success(points)

    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun getPointById(id: Int):
            ServiceResult<RecyclePoint> = try {
        dbQuery {
            RecyclePointTable.join(
                otherTable = RecyclePointTypeTable,
                joinType = JoinType.INNER,
                onColumn = RecyclePointTable.type,
                otherColumn = RecyclePointTypeTable.id
            ).slice(pointsFieldSet)
                .select { RecyclePointTable.id eq id }
                .singleOrNull()
                ?.let {
                    ServiceResult.Success(it.toRecyclePoint())
                } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }


    override suspend fun getPointByQuery(query: String):
            ServiceResult<List<RecyclePoint>> = try {
        dbQuery {
            val points = (RecyclePointTable innerJoin
                    RecyclePointTypeTable).select {
                concat(
                    separator = ",",
                    expr = listOf(
                        RecyclePointTable.streetName.lowerCase(),
                        RecyclePointTable.streetHouseNum
                    )
                ) like query.lowercase().plus("%")
            }.toList().map {
                it.toRecyclePoint()
            }
            ServiceResult.Success(points)
        }
    } catch (e: Exception) {
        println(e.message)
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun suchPointDoesNotExist(recyclePoint: RecyclePoint):
            ServiceResult<Boolean> = try {
        val doesNotExist = dbQuery {
            RecyclePointTable.select {
                (RecyclePointTable.streetName eq recyclePoint.streetName) and
                        (RecyclePointTable.streetHouseNum eq recyclePoint.streetHouseNum)
            }
                .count() == 0L
        }
        ServiceResult.Success(doesNotExist)
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun deletePoint(idPoint: Int): ServiceResult<Boolean> {
        return try {
            val deleteSucceeded = dbQuery {
                RecyclePointTable.deleteWhere { RecyclePointTable.id eq idPoint }
            } > 0
            ServiceResult.Success(deleteSucceeded)
        } catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun registerPoint(point: RecyclePoint):
            ServiceResult<RecyclePoint> = try {

        when (val typeId =
            getRecyclePointIdByType(point.type!!)) {
            is ServiceResult.Success -> {

                dbQuery {
                    RecyclePointTable.insert {
                        it[latitude] = point.latitude
                        it[longitude] = point.longitude
                        it[streetName] = point.streetName
                        it[streetHouseNum] = point.streetHouseNum
                        it[weekSchedule] = point.weekSchedule
                        it[description] =
                            point.locationDescription
                        it[type] = typeId.data
                    }.resultedValues?.singleOrNull()?.let {
                        ServiceResult.Success(it.toRecyclePoint())
                    } ?: ServiceResult.Error(Errors.INSERT_FAILED)
                }
            }

            is ServiceResult.Error -> {
                ServiceResult.Error(Errors.NOT_FOUND)
            }
        }
    } catch (e: Exception) {
        println(e.message)
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun insertPhotoPath(
        idPoint: Int,
        photoPath: String,
    ) = try {
        val updateSucceeded = dbQuery {
            RecyclePointTable
                .update(where = { RecyclePointTable.id eq idPoint },
                    body =
                    {
                        it[RecyclePointTable.photoPath] = "${
                            Const
                                .PHOTO_URL
                        }/$photoPath"
                    }) > 0
        }
        ServiceResult.Success(updateSucceeded)
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun uploadChannelPhoto(
        photoChannel: ByteReadChannel,
        photoName: String,
    ) = try {
        val path = "${Const.PHOTO_PATH}\\$photoName"
        photoChannel.copyAndClose(File(path).writeChannel())

        ServiceResult.Success(File(path).exists())
    } catch (e: Exception) {
        ServiceResult.Error(Errors.FILE_SYSTEM_ERROR)
    }


    override suspend fun addAcceptedRubbishType(
        idRecyclePoint: Int,
        type: Int,
    ) = try {
        dbQuery {
            if (RecyclePointRubbishTypeTable.insert {
                    it[recyclePoint] = idRecyclePoint
                    it[rubbishType] = type
                }.resultedValues.isNullOrEmpty())
                ServiceResult.Error(Errors.INSERT_FAILED)
            else ServiceResult.Success(true)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun getPointsFilteredByType(types: List<String>):
            ServiceResult<List<RecyclePoint>> = try {
        dbQuery {
            val points = RecyclePointRubbishTypeTable
                .innerJoin(RecyclePointTable)
                .innerJoin(RubbishTypeTable).select {
                    RubbishTypeTable.type.lowerCase() inList types.map { it
                        .lowercase() }
                }.toList().map { it.toRecyclePoint() }
            ServiceResult.Success(points)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun changePointApprovalById(
        idPoint: Int,
        approval: Boolean,
    ): ServiceResult<Boolean> = try {
        dbQuery {
            val updateSucceeded = RecyclePointTable
                .update(where = {
                    RecyclePointTable.id eq idPoint
                },
                    body = { it[approved] = approval }) > 0

            ServiceResult.Success(updateSucceeded)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun getPointApprovalById(idPoint: Int):
            ServiceResult<Boolean> = try {
        dbQuery {
            RecyclePointTable.select { RecyclePointTable.id eq idPoint }
                .adjustSlice {
                    slice(RecyclePointTable.approved)
                }.singleOrNull()?.let {
                    ServiceResult.Success(it[RecyclePointTable.approved])
                } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    private fun ResultRow.toRecyclePoint() = RecyclePoint(
        id = this[RecyclePointTable.id].value,
        latitude = this[RecyclePointTable.latitude],
        longitude = this[RecyclePointTable.longitude],
        streetName = this[RecyclePointTable.streetName],
        streetHouseNum = this[RecyclePointTable.streetHouseNum],
        weekSchedule = this[RecyclePointTable.weekSchedule],
        locationDescription = this[RecyclePointTable
            .description],
        photoPath = this[RecyclePointTable.photoPath],
        totalRating = this[RecyclePointTable.totalRating],
        approved = this[RecyclePointTable.approved],
    )

    private suspend fun getRecyclePointIdByType(type: String):
            ServiceResult<Int> = try {
        dbQuery {
            RecyclePointTypeTable.select { RecyclePointTypeTable.type eq type }
                .adjustSlice {
                    slice(RecyclePointTypeTable.id)
                }.singleOrNull()?.let {
                    ServiceResult.Success(it[RecyclePointTypeTable.id].value)
                } ?: ServiceResult.Error(Errors.NOT_FOUND)
        }
    } catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    private val pointsFieldSet = listOf(
        RecyclePointTable.id,
        RecyclePointTable.latitude,
        RecyclePointTable.longitude,
        RecyclePointTable.streetName,
        RecyclePointTable.streetHouseNum,
        RecyclePointTable.weekSchedule,
        RecyclePointTable.description,
        RecyclePointTable.photoPath,
        RecyclePointTable.totalRating,
        RecyclePointTable.approved,
        RecyclePointTypeTable.type
    )
}