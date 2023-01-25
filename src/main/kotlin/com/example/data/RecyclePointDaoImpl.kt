package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.domain.dao.RecyclePointDao
import com.example.entity.RecyclePoint
import com.example.utils.Errors
import com.example.utils.Const
import com.example.utils.ServiceResult
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.io.File

class RecyclePointDaoImpl: RecyclePointDao {

    override suspend fun getPoints(): ServiceResult<List<RecyclePoint>> {

        return try {
            val points = dbQuery {
                RecyclePointTable.join(
                    otherTable = RecyclePointTypeTable,
                    joinType = JoinType.INNER,
                    onColumn = RecyclePointTable.type,
                    otherColumn = RecyclePointTypeTable.id
                ).slice(
                    RecyclePointTable.id,
                    RecyclePointTable.latitude,
                    RecyclePointTable.longitude,
                    RecyclePointTable.streetName,
                    RecyclePointTable.streetHouseNum,
                    RecyclePointTable.description,
                    RecyclePointTable.photoPath,
                    RecyclePointTable.totalRating,
                    RecyclePointTable.totalReviews,
                    RecyclePointTable.approved,
                    RecyclePointTypeTable.type
                ).selectAll().toList().map {
                    rowToRecyclePoint(it)
                }
            }

            if (points.isNotEmpty()) {
                ServiceResult.Success(points)
            }
            else {
                ServiceResult.Error(Errors.EMPTY_DATA)
            }

        }
        catch (e: Exception) {
            println(e.message)
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getPointById(id: Int): ServiceResult<RecyclePoint> {
        return try {
            dbQuery {
                RecyclePointTable.join(
                    otherTable = RecyclePointTypeTable,
                    joinType = JoinType.INNER,
                    onColumn = RecyclePointTable.type,
                    otherColumn = RecyclePointTypeTable.id
                ).slice(
                    RecyclePointTable.id,
                    RecyclePointTable.latitude,
                    RecyclePointTable.longitude,
                    RecyclePointTable.streetName,
                    RecyclePointTable.streetHouseNum,
                    RecyclePointTable.description,
                    RecyclePointTable.photoPath,
                    RecyclePointTable.totalRating,
                    RecyclePointTable.totalReviews,
                    RecyclePointTable.approved,
                    RecyclePointTypeTable.type).select { RecyclePointTable.id eq id }.singleOrNull()?.let {
                    ServiceResult.Success(rowToRecyclePoint(it))
                } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun suchPointDoesNotExist(recyclePoint: RecyclePoint):
            ServiceResult<Boolean> {

        return try {
            if (dbQuery {
                RecyclePointTable.select {
                    (RecyclePointTable.streetName eq recyclePoint.streetName) and
                            (RecyclePointTable.streetHouseNum eq recyclePoint.streetHouseNum) }
                    .count() < 1
            }) ServiceResult.Success(true)
            else ServiceResult.Error(Errors.ALREADY_EXISTS)


        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun deletePoint(idPoint: Int): ServiceResult<Boolean> {
        return try {
            dbQuery {
                RecyclePointTable.deleteWhere { RecyclePointTable.id eq idPoint }
            }
            ServiceResult.Success(true)
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun registerPoint(point: RecyclePoint): ServiceResult<Int> {

        return try {

            when (val typeId = getRecyclePointIdByType(point.type)) {
                is ServiceResult.Success -> {

                    dbQuery {
                        RecyclePointTable.insertAndGetId {
                            it[latitude] = point.latitude
                            it[longitude] = point.longitude
                            it[streetName] = point.streetName
                            it[streetHouseNum] = point.streetHouseNum
                            it[description] = point.locationDescription
                            it[type] = typeId.data
                        }.value.let {
                            ServiceResult.Success(it)
                        }

                    }
                }

                is ServiceResult.Error -> {
                    ServiceResult.Error(Errors.INSERT_FAILED)
                }
            }

        }
        catch (e: Exception) {
            println(e.message)
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun insertPhotoPath(idPoint: Int, photoPath: String): ServiceResult<Boolean> {
        return try {
            dbQuery { RecyclePointTable.update(where = {RecyclePointTable.id eq idPoint}, body =
            {it[RecyclePointTable.photoPath] = photoPath}) }
            ServiceResult.Success(true)
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun uploadMultipartPhoto(photoBytes: ByteArray, photoName: String):
            ServiceResult<Boolean> {

        return try {
            val path = "${Const.PHOTO_PATH}/$photoName"
            File(path).writeBytes(photoBytes)

            if (File(path).exists()) ServiceResult.Success(true)
            else ServiceResult.Error(Errors.FILE_SYSTEM_ERROR)
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.FILE_SYSTEM_ERROR)
        }
    }

    override suspend fun changePointApprovalById(idPoint: Int, approval: Boolean):
            ServiceResult<Boolean> {
        return try {
            dbQuery {

                if (RecyclePointTable.update(where = { RecyclePointTable.id eq idPoint },
                    body = { it[approved] = approval }) > 0)
                    ServiceResult.Success(true)

                else ServiceResult.Error(Errors.UPDATE_FAILED)

            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    override suspend fun getPointApprovalById(idPoint: Int): ServiceResult<Boolean> {
        return try {
            dbQuery {
                RecyclePointTable.select { RecyclePointTable.id eq idPoint }.adjustSlice {
                    slice(RecyclePointTable.approved) }.singleOrNull()?.let {
                        ServiceResult.Success(it[RecyclePointTable.approved])
                } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }

    private fun rowToRecyclePoint(row: ResultRow): RecyclePoint {
        return RecyclePoint(
            id = row[RecyclePointTable.id].value,
            latitude = row[RecyclePointTable.latitude],
            longitude = row[RecyclePointTable.longitude],
            streetName = row[RecyclePointTable.streetName],
            streetHouseNum = row[RecyclePointTable.streetHouseNum],
            locationDescription = row[RecyclePointTable.description],
            photoPath = row[RecyclePointTable.photoPath],
            totalRating = row[RecyclePointTable.totalRating],
            totalReviews = row[RecyclePointTable.totalReviews],
            approved = row[RecyclePointTable.approved],
            type = row[RecyclePointTypeTable.type],
        )
    }

    private suspend fun getRecyclePointIdByType(type: String): ServiceResult<Int> {
        return try {
            dbQuery { RecyclePointTypeTable.select { RecyclePointTypeTable.type eq type }.adjustSlice {
                slice(RecyclePointTypeTable.id) }.singleOrNull()?.let {
                    ServiceResult.Success(it[RecyclePointTypeTable.id].value)
            } ?: ServiceResult.Error(Errors.ID_NOT_FOUND)
            }
        }
        catch (e: Exception) {
            ServiceResult.Error(Errors.DATABASE_ERROR)
        }
    }
}