package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class ChangePassword(
    /* in case `forgot password` */
    val previousPassword: String? = null,
    val newPassword1: String,
    val newPassword2: String
)
