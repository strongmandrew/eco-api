package com.example.routes

enum class Endpoint(val path: String) {
    HOME(path = "/"),
    API("/api"),
    RECYCLE_POINT(path = "/recycle_point"),
    REVIEW(path = "/review"),
    RUBBISH_TYPE(path = "/rubbish_type"),
    NEWS(path = "/news"),
    APPROVE(path ="/approve"),
    PHOTO(path = "/photo"),
    USER("/user"),
    REGISTER("/register"),
    VALIDATE("/validate"),
    SEND("/send"),
    AUTHORIZE("/authorize"),
    TAKE_OFF("/take_off"),
    TOTAL("/total"),
    PROFILE("/profile"),
    CHANGE_PASSWORD("/change_password")
}