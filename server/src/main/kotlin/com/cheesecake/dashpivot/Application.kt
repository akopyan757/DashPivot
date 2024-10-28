package com.cheesecake.dashpivot

import com.cheesecake.common.api.SERVER_PORT
import com.cheesecake.common.api.VersionInfo
import com.cheesecake.dashpivot.database.connectToDatabase
import com.cheesecake.dashpivot.di.serverDI
import com.cheesecake.dashpivot.json.serverJson
import com.cheesecake.server.auth.route.authRoute
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

val PORT = System.getenv("PORT")?.toInt() ?: SERVER_PORT

fun main() {
    embeddedServer(Netty, port = PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    connectToDatabase()
    serverJson()
    val di = serverDI()

    routing {
        authRoute(di)
        get("/") { call.respondText("Hello, server!\nVersion: ${VersionInfo.SERVER_VERSION}") }
    }
}