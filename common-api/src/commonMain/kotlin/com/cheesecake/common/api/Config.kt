package com.cheesecake.common.api

const val SERVER_PORT = 8085

expect fun host(): String

//val BASE_URL = "${host()}:$SERVER_PORT"
val BASE_URL = "https://dash-pivot-app-990ab78e109f.herokuapp.com"