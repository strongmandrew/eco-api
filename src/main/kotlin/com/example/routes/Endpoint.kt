package com.example.routes

enum class Endpoint(val path: String) {
    HOME(path = "/"),
    RECYCLE_POINT(path = "/recycle_point"),
    REVIEW(path = "/review"),
    RUBBISH_TYPE(path = "/rubbish_type"),
    NEWS(path = "/news"),
    APPROVE(path ="/approve"),
    PHOTO(path = "/photo"),
    USER("/user"),
    REGISTER("/register")
}