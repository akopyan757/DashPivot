package com.cheesecake.common.api

interface ApiError {
    val code: Int
    val message: String
}