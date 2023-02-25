package com.example.domain.usecase.user.jwt

enum class Role(val id: Int) {
    USER(1),
    ADMIN(2),
    GOD(3)
}