package com.cheesecake.dashpivot

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform