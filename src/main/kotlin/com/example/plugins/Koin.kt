package com.example.plugins

import com.example.data.RecyclePointDaoImpl
import com.example.domain.dao.RecyclePointDao
import com.example.domain.usecase.recyclePoint.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {

    install(Koin) {
        slf4jLogger()
        modules(listOf(recyclePointModule))
    }
}

val recyclePointModule = module {
    single<RecyclePointDao> { RecyclePointDaoImpl() }

    single { ChangeRecyclePointApproval(get()) }
    single { GetRecyclePointById(get()) }
    single { GetRecyclePoints(get()) }
    single { InsertRecyclePoint(get()) }
    single { SetRecyclePointPhoto(get()) }
    single { DeleteRecyclePoint(get()) }
}