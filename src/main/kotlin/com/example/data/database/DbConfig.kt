package com.example.data.database

sealed class DbConfig(
    val dbNameConfig: String,
    envHost: String,
    envPort: String,
    envConnection: String,
    envUser: String,
    envPassword: String,
    val driver: String,
) {
    private val host = System.getenv(envHost)
    private val port = System.getenv(envPort)
    private val connection = System.getenv(envConnection)
    val user: String = System.getenv(envUser)
    val password: String = System.getenv(envPassword)

    val url: String
        get() = "$dbNameConfig://$host:$port/$connection"

    object MySqlConfig: DbConfig(
        dbNameConfig = "jdbc:mysql",
        envHost = "mysql.host",
        envPort = "mysql.port",
        envConnection = "mysql.connection",
        envUser = "mysql.user",
        envPassword = "mysql.password",
        driver = "com.mysql.cj.jdbc.Driver"
    )


}
