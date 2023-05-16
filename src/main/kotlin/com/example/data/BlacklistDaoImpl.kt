package com.example.data

import com.example.data.database.DatabaseFactory.dbQuery
import com.example.data.database.EmailBlacklistTable
import com.example.domain.dao.BlacklistDao
import com.example.utils.Errors
import com.example.utils.ServiceResult
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class BlacklistDaoImpl: BlacklistDao {

    override suspend fun getAllBlacklisted() = try {
        dbQuery {
            val list = EmailBlacklistTable.selectAll().toList().map {
                it[EmailBlacklistTable.email]
            }
            ServiceResult.Success(list)
        }
    }
    catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun blackListEmail(email: String) = try {
        dbQuery {
            if (EmailBlacklistTable.insert {
                it[this.email] = email
            }.resultedValues.isNullOrEmpty())
                ServiceResult.Error(Errors.INSERT_FAILED)
            else ServiceResult.Success(true)
        }
    }
    catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }

    override suspend fun removeFromBlacklist(email: String) = try {
        dbQuery {
            if (EmailBlacklistTable.deleteWhere { this.email eq
                        email } > 0)
                ServiceResult.Success(true)
            else ServiceResult.Error(Errors.NOT_FOUND)
        }
    }
    catch (e: Exception) {
        ServiceResult.Error(Errors.DATABASE_ERROR)
    }
}