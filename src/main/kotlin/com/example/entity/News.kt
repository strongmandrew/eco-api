package com.example.entity

import kotlinx.serialization.Serializable

@Serializable
data class News(
    val id: Int? = null,
    val title: String,
    val imageUrl: String,
    val dateOf: String,
    val url: String
)
