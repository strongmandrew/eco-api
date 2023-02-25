package com.example.domain.usecase.user.jwt

object JWTCredentials {

    private const val JWT_ISSUER = "jwt.issuer"
    private const val JWT_SECRET = "jwt.secret"

    val jwtIss by lazy {
        System.getenv(JWT_ISSUER)
    }
    val jwtSecret by lazy {
        System.getenv(JWT_SECRET)
    }

}