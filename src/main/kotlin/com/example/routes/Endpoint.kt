package com.example.routes

enum class Endpoint(val path: String) {
    HOME(path = "/"),
    RECYCLE_POINT(path = "/recycle_point"),
    REVIEW(path = "/review"),
    LOGIN(path = "/user"),
    RUBBISH_TYPE(path = "/rubbish_type"),
    NEWS("/news"),
    APPROVE("/approve"),
    PHOTO("/photo")
}