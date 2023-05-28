package com.example.data.database

import com.example.utils.EnvProvider

sealed class DbConfig(
    val dbNameConfig: String,
    val host: String,
    val port: String,
    val schema: String,
    val user: String,
    val password: String,
    val driver: String,
) {
    val url: String
        get() = "$dbNameConfig://$host:$port/$schema"

    object MySqlConfig: DbConfig(
        dbNameConfig = "jdbc:mysql",
        host = EnvProvider.mySqlHost,
        port = EnvProvider.mySqlPort,
        schema = EnvProvider.mySqlSchema,
        user = EnvProvider.mySqlUser,
        password = EnvProvider.mySqlPassword,
        driver = "com.mysql.cj.jdbc.Driver"
    )


}
