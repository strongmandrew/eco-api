package com.example.domain.usecase.email

object EmailCredentials {
    private const val EMAIL_ADRESS_FROM = "eco.email.from"
    private const val EMAIL_ADRESS_INFO = "eco.email.account"
    private const val EMAIL_PROFILE = "eco.email.profile"
    private const val EMAIL_PASSWORD = "eco.email.password"

    val emailFrom by lazy {
        System.getenv(EMAIL_ADRESS_FROM)
    }

    val emailAccount by lazy {
        System.getenv(EMAIL_ADRESS_INFO)
    }

    val emailProfile by lazy {
        System.getenv(EMAIL_PROFILE)
    }

    val emailPassword by lazy {
        System.getenv(EMAIL_PASSWORD)
    }
}