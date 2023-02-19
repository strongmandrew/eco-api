package com.example.domain.usecase.user

import io.ktor.util.*
import java.math.BigInteger

class PasswordEncrypt {

    companion object {
        private const val HASH_FORMAT = "%040x"
    }

    operator fun invoke(password: String): String {

        val passwordBytes = sha1(password.toByteArray())

        return String.format(HASH_FORMAT, BigInteger(1, passwordBytes))
    }
}