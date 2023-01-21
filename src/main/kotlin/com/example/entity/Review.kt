package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: Int? = null,
    val reviewText: String,
    val dateOf: String,
    val rating: Int,
    val pointId: Int,
    val userId: Int
)
