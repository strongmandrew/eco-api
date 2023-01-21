package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val nickname: String,
    val email: String,
    val password: String? = null,
    val role: String? = null
)
