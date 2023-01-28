package com.example.plugins

import com.example.data.NewsDaoImpl
import com.example.data.RecyclePointDaoImpl
import com.example.data.ReviewDaoImpl
import com.example.domain.dao.NewsDao
import com.example.domain.dao.RecyclePointDao
import com.example.domain.dao.ReviewDao
import com.example.domain.usecase.news.GetNews
import com.example.domain.usecase.recyclePoint.*
import com.example.domain.usecase.review.DeleteReviewById
import com.example.domain.usecase.review.GetReviewById
import com.example.domain.usecase.review.GetReviewsByPointId
import com.example.domain.usecase.review.InsertReview
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {

    install(Koin) {
        slf4jLogger()
        modules(listOf(recyclePointModule, reviewModule, newsModule))
    }
}

val recyclePointModule = module {
    single<RecyclePointDao> { RecyclePointDaoImpl() }

    factory { RecyclePointFileNameGenerator() }

    single { ChangeRecyclePointApproval(get()) }
    single { GetRecyclePointById(get()) }
    single { GetRecyclePoints(get()) }
    single { InsertRecyclePoint(get()) }
    single { SetRecyclePointPhoto(get(), get()) }
    single { DeleteRecyclePoint(get()) }
}

val reviewModule = module {

    single<ReviewDao> {ReviewDaoImpl()}

    single { GetReviewsByPointId(get()) }
    single { GetReviewById(get()) }
    single { InsertReview(get()) }
    single { DeleteReviewById(get()) }
}

val newsModule = module {
    single<NewsDao> { NewsDaoImpl() }

    single { GetNews(get()) }
}