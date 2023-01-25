package com.example

import com.example.data.database.DatabaseFactory
import io.ktor.server.application.*
import com.example.plugins.*
import com.example.data.database.DbConfig.*
import com.example.domain.usecase.recyclePoint.GetRecyclePointById
import com.example.domain.usecase.recyclePoint.GetRecyclePoints
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
// application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    DatabaseFactory.init(dbConfig = MySqlConfig)
    configureSerialization()
    configureRouting()
    configureMonitoring()
    configureKoin()
    //configureHTTP()
    configureSecurity()

}
