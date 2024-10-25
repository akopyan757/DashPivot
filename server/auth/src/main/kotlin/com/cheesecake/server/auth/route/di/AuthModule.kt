package com.cheesecake.server.auth.route.di

import com.cheesecake.common.auth.service.UserService
import com.cheesecake.server.auth.route.mail.EmailService
import com.cheesecake.server.auth.route.mail.IEmailService
import com.cheesecake.server.auth.route.repository.UserRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val authModule = DI.Module("AuthModule") {
    bind<IEmailService>() with singleton { EmailService() }
    bind<UserService>() with singleton { UserRepository(instance()) }
}