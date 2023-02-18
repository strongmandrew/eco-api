package com.example.domain.usecase.email

import java.util.Properties

object EmailCredentials {
    private const val EMAIL_ADDRESS_FROM = "eco.email.from"
    private const val EMAIL_ADDRESS_INFO = "eco.email.account"
    private const val EMAIL_PROFILE = "eco.email.profile"
    private const val EMAIL_PASSWORD = "eco.email.password"

    const val MAIL_HOST = "mail.smtp.host"
    const val MAIL_PORT = "mail.smtp.port"
    const val MAIL_AUTH = "mail.smtp.auth"
    const val MAIL_STARTTLS = "mail.smtp.starttls.enable"

    val props by lazy {
        Properties().apply {
            put(MAIL_HOST, mailHost)
            put(MAIL_PORT, mailPort)
            put(MAIL_AUTH, mailAuth)
            put(MAIL_STARTTLS, mailStartTls)
        }
    }

    val emailFrom by lazy {
        System.getenv(EMAIL_ADDRESS_FROM)
    }
    val emailAccount by lazy {
        System.getenv(EMAIL_ADDRESS_INFO)
    }
    val emailProfile by lazy {
        System.getenv(EMAIL_PROFILE)
    }
    val emailPassword by lazy {
        System.getenv(EMAIL_PASSWORD)
    }
    val mailHost by lazy {
        System.getenv(MAIL_HOST)
    }
    val mailPort by lazy {
        System.getenv(MAIL_PORT)
    }
    val mailAuth by lazy {
        System.getenv(MAIL_AUTH)
    }
    val mailStartTls by lazy {
        System.getenv(MAIL_STARTTLS)
    }

}