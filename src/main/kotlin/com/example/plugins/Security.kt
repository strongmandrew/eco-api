package com.example.plugins

import io.ktor.server.sessions.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    
    authentication {
            jwt {
                val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
                realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
                verifier(
                    JWT
                        .require(Algorithm.HMAC256("secret"))
                        .withAudience(jwtAudience)
                        .withIssuer(this@configureSecurity.environment.config.property("jwt.domain").getString())
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
                }
            }
        }

}
