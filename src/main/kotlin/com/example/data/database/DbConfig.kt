package com.example.data.database

sealed class DbConfig(
    val envHost: String,
    val envPort: String,
    val envConnection: String,
    val envUser: String,
    val envPassword: String,
) {


    object MySqlConfig: DbConfig(
        envHost = "mysql.host",
        envPort = "mysql.port",
        envConnection = "mysql.connection",
        envUser = "mysql.user",
        envPassword = "mysql.password",
    )


}
