package com.axel_stein.pizzatestappcompose.data.model

class ApiError(
    val code: Int,
    msg: String
): Throwable(msg)