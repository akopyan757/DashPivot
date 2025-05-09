package com.cheesecake.common.api

expect object Log {
    fun info(tag: String, message: String)
    fun debug(tag: String, message: String)
    fun error(tag: String, exception: Exception)
}