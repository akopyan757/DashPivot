package com.cheesecake.dashpivot.di

import com.cheesecake.server.auth.route.di.authModule
import io.ktor.server.application.Application
import org.kodein.di.ktor.di
import org.kodein.di.DI

val serverModule = DI {
    import(authModule)
}

fun Application.serverDI(): DI {
    di { serverModule }
    return serverModule
}