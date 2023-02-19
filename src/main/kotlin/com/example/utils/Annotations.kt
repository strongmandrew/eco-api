package com.example.utils

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class Unused

@Target(AnnotationTarget.FUNCTION)
annotation class DevOnly