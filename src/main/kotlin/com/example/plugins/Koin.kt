package com.example.plugins

import com.example.data.*
import com.example.domain.dao.*
import com.example.domain.usecase.email.CodeGenerator
import com.example.domain.usecase.email.EmailAuthenticator
import com.example.domain.usecase.email.EmailCredentials
import com.example.domain.usecase.email.EmailService
import com.example.domain.usecase.news.GetNews
import com.example.domain.usecase.recyclePoint.*
import com.example.domain.usecase.review.DeleteReviewById
import com.example.domain.usecase.review.GetReviewById
import com.example.domain.usecase.review.GetReviewsByPointId
import com.example.domain.usecase.review.InsertReview
import com.example.domain.usecase.rubbishType.DeleteRubbishTypeById
import com.example.domain.usecase.rubbishType.GetRubbishTypeById
import com.example.domain.usecase.rubbishType.GetTotalRubbishTypeTakeOff
import com.example.domain.usecase.rubbishType.InsertRubbishType
import com.example.domain.usecase.user.*
import com.example.domain.usecase.user.jwt.JWTHandler
import com.example.domain.usecase.userTakeOff.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import javax.mail.Session

fun Application.configureKoin() {

    install(Koin) {
        slf4jLogger()
        modules(listOf(recyclePointModule, reviewModule,
            newsModule, userModule, rubbishTypeModule, userTakeOffModule))
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
    single { DownloadPointPhoto(get()) }
    single { DeleteRecyclePoint(get()) }
    single { GetRecyclePointByQuery(get()) }
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

val userModule = module {

    factory { PasswordEncrypt() }

    single<UserDao> { UserDaoImpl(get()) }

    val session = Session.getInstance(EmailCredentials.props,
        EmailAuthenticator().invoke())

    factory { JWTHandler() }
    factory { EmailService(session) }
    factory { CodeGenerator() }
    single { RegisterUser(get()) }
    single { SetOrUpdateValidationCode(get()) }
    single { SendValidation(get(), get(), get()) }
    single { ApproveValidation(get()) }
    single { GetUserByEmail(get()) }
    single { AuthorizeUser(get(), get(), get()) }
}

val rubbishTypeModule = module {
    single<RubbishTypeDao> { RubbishTypeDaoImpl() }
    single { GetRubbishTypeById(get()) }
    single { InsertRubbishType(get()) }
    single { GetTotalRubbishTypeTakeOff(get()) }
    single { DeleteRubbishTypeById(get()) }
}

val userTakeOffModule = module {
    single<UserTakeOffDao> { UserTakeOffDaoImpl() }
    single { GetTotalUserTakeOff(get()) }
    single { GetTakeOffById(get()) }
    single { TakeOffRubbish(get()) }
    single { GetAllUserTakeOffs(get()) }
    single { DeleteTakeOffById(get()) }
}