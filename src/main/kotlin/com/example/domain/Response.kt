package com.example.domain

import com.example.utils.Errors

data class Response<T>(
    val data: T? = null,
    val errors: List<Errors> = emptyList()
)
