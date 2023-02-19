package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String? = null,
    val dateOfBirth: String,
    val dateOfRegistration: String? = null,
    val userImage: String,
    val emailVerified: Boolean? = null,
    val timesChanged: Int? = null,
    val role: Int? = null
)
