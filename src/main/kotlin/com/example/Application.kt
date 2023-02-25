package com.example

import com.example.data.database.DatabaseFactory
import io.ktor.server.application.*
import com.example.plugins.*
import com.example.data.database.DbConfig.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
// application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    DatabaseFactory.init(dbConfig = MySqlConfig)
    configureSerialization()
    configureSecurity()
    configureRouting()
    configureMonitoring()
    configureKoin()
    //configureHTTP()


}
