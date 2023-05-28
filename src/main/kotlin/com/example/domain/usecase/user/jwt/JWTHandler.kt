package com.example.domain.usecase.user.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.utils.EnvProvider

class JWTHandler {

    companion object {
        const val JWT_USER_ID = "uid"
        const val JWT_ROLE_ID = "rid"
        const val JWT_TIMES_CHANGED = "tch"
    }

    fun generateToken(
        userId: Int,
        role: Int,
        timesCredentialsChanged: Int
    ): String {

        return JWT.create()
            .withIssuer(EnvProvider.jwtIssuer)
            .withClaim(JWT_USER_ID, userId)
            .withClaim(JWT_ROLE_ID, role)
            .withClaim(JWT_TIMES_CHANGED, timesCredentialsChanged)
            .sign(Algorithm.HMAC256(EnvProvider.jwtSecret))


    }
}