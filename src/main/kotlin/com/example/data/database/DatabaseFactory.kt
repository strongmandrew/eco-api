package com.example.data.database

import ch.qos.logback.classic.Logger
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseFactory {

    fun init(dbConfig: DbConfig) {

        val host = System.getenv(dbConfig.envHost)
        val port = System.getenv(dbConfig.envPort)
        val connection = System.getenv(dbConfig.envConnection)
        val user = System.getenv(dbConfig.envUser)
        val password = System.getenv(dbConfig.envPassword)

        Database.connect(
            url = "jdbc:mysql://$host:$port/$connection",
            driver = "com.mysql.cj.jdbc.Driver",
            user = user,
            password = password
        )

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) {
            block()
    }
}