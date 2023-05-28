package com.example.domain.usecase.email

import com.example.utils.EnvProvider
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication


class EmailAuthenticator {

    operator fun invoke(): Authenticator {

        return object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(
                    EnvProvider.emailProfile,
                    EnvProvider.emailPassword
                )
            }
        }
    }
}