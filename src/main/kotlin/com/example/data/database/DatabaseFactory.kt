package com.example.data.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {

    suspend fun init(dbConfig: DbConfig) {

        Database.connect(
            url = dbConfig.url,
            driver = dbConfig.driver,
            user = dbConfig.user,
            password = dbConfig.password
        )

        dbQuery {
            SchemaUtils.createMissingTablesAndColumns(
                UserTakeOffTable,
                UserTable,
                ReviewTable,
                RecyclePointTable,
                RecyclePointTypeTable,
                RecyclePointRubbishTypeTable,
                RubbishTypeTable,
                EmailBlacklistTable,
                RoleTable,
                UserEmailCodeTable,
            )
        }

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) {
            block()
    }
}