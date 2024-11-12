package com.cheesecake.server.auth.route.di

import com.cheesecake.common.auth.service.UserService
import com.cheesecake.server.auth.route.database.IUserSource
import com.cheesecake.server.auth.route.database.UserSource
import com.cheesecake.server.auth.route.mail.EmailService
import com.cheesecake.server.auth.route.mail.IEmailService
import com.cheesecake.server.auth.route.repository.UserRepository
import com.cheesecake.server.auth.route.utils.IVerifyCodeGenerator
import com.cheesecake.server.auth.route.utils.IPasswordHasher
import com.cheesecake.server.auth.route.utils.ITokenGenerator
import com.cheesecake.server.auth.route.utils.PasswordHasher
import com.cheesecake.server.auth.route.utils.TokenGenerator
import com.cheesecake.server.auth.route.utils.VerifyCodeGenerator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val authModule = DI.Module("AuthModule") {
    bind<IPasswordHasher>() with singleton { PasswordHasher() }
    bind<IEmailService>() with singleton { EmailService() }
    bind<IVerifyCodeGenerator>() with singleton { VerifyCodeGenerator() }
    bind<IUserSource>() with singleton { UserSource() }
    bind<ITokenGenerator>() with singleton { TokenGenerator }
    bind<UserService>() with singleton {
        UserRepository(instance(), instance(), instance(), instance(), instance())
    }
}