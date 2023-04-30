package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: Int? = null,
    val reviewText: String,
    val dateOf: String? = null,
    val pointId: Int? = null,
    val userId: Int = 1
)
