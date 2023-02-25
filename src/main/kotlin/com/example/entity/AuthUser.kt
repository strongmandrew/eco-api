package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    val email: String,
    val password: String
)
