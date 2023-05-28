package com.example.utils

import java.util.*

object EnvProvider {

    private const val EMAIL_ADDRESS_FROM = "eco.email.from"
    private const val EMAIL_ADDRESS_INFO = "eco.email.account"
    private const val EMAIL_PROFILE = "eco.email.profile"
    private const val EMAIL_PASSWORD = "eco.email.password"

    private const val MAIL_HOST = "mail.smtp.host"
    private const val MAIL_PORT = "mail.smtp.port"
    private const val MAIL_AUTH = "mail.smtp.auth"
    private const val MAIL_STARTTLS = "mail.smtp.starttls.enable"

    private const val JWT_ISSUER = "jwt.issuer"
    private const val JWT_SECRET = "jwt.secret"

    private const val MY_SQL_HOST = "mysql.host"
    private const val MY_SQL_PORT = "mysql.port"
    private const val MY_SQL_CONNECTION = "mysql.connection"
    private const val MY_SQL_USER = "mysql.user"
    private const val MY_SQL_PASSWORD = "mysql.password"

    /**
     * Specifies who's responsible for giving out JWT e.g. `EcoApi`
     */
    val jwtIssuer: String by lazy {
        System.getenv(JWT_ISSUER)
    }

    /**
     * Specifies what is JWT secret
     */
    val jwtSecret: String by lazy {
        System.getenv(JWT_SECRET)
    }

    /**
     * Specifies MySQL server host
     */
    val mySqlHost: String by lazy {
        System.getenv(MY_SQL_HOST)
    }

    /**
     * Specifies MySQL server port
     */
    val mySqlPort: String by lazy {
        System.getenv(MY_SQL_PORT)
    }

    /**
     * Specifies MySQL server schema
     */
    val mySqlSchema: String by lazy {
        System.getenv(MY_SQL_CONNECTION)
    }

    /**
     * Specifies MySQL user
     */
    val mySqlUser: String by lazy {
        System.getenv(MY_SQL_USER)
    }

    /**
     * Specifies MySQL user password
     */
    val mySqlPassword: String by lazy {
        System.getenv(MY_SQL_PASSWORD)
    }

    /**
     * Properties to configure javax.mail client
     */
    val mailProps by lazy {
        Properties().apply {
            put(MAIL_HOST, System.getenv(MAIL_HOST))
            put(MAIL_PORT, System.getenv(MAIL_PORT))
            put(MAIL_AUTH, System.getenv(MAIL_AUTH))
            put(MAIL_STARTTLS, System.getenv(MAIL_STARTTLS))
        }
    }

    /**
     * How sender email LOOKS like e.g. `no-reply@gmail.com`
     */
    val emailFrom: String by lazy {
        System.getenv(EMAIL_ADDRESS_FROM)
    }

    /**
     * How sender account name LOOKS like e.g. `NoReply-Eco`
     */
    val emailAccount: String by lazy {
        System.getenv(EMAIL_ADDRESS_INFO)
    }

    /**
     * True email sender profile e.g. `abcdef@gmail.com`
     */
    val emailProfile: String by lazy {
        System.getenv(EMAIL_PROFILE)
    }

    /**
     * True email sender password e.g. `12345`
     */
    val emailPassword: String by lazy {
        System.getenv(EMAIL_PASSWORD)
    }
}