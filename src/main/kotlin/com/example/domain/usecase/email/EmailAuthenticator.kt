package com.example.domain.usecase.email

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication


class EmailAuthenticator {

    operator fun invoke(): Authenticator {

        return object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EmailCredentials
                    .emailProfile, EmailCredentials.emailPassword)
            }
        }
    }
}