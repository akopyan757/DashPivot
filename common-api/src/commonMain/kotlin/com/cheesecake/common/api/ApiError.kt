package com.cheesecake.common.api

interface ApiError {
    val message: String

    companion object {
        object UNKNOWN : ApiError {
            override val message: String = "Unknown error"
        }
    }
}