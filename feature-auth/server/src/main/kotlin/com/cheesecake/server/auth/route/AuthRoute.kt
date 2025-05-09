package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.api.EndPoint
import com.cheesecake.common.auth.model.changePassword.ResetPasswordRequest
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import com.cheesecake.common.auth.model.verefication.VerificationRequest
import com.cheesecake.common.auth.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.util.pipeline.PipelineContext
import org.kodein.di.DI
import org.kodein.di.instance

fun Route.authRoute(di: DI) {
    post(EndPoint.REGISTER.path) {
        val registerRequest = call.receive<RegisterRequest>()
        val userRepository: UserService by di.instance()

        when (val result = userRepository.registerUser(registerRequest)) {
            is ApiResult.Success -> call.respond(HttpStatusCode.Created, result.data)
            is ApiResult.Error -> handleError(result)
        }
    }
    post(EndPoint.REGISTER_CONFIRM.path) {
        val verificationRequest = call.receive<VerificationRequest>()
        val userRepository: UserService by di.instance()

        when (val result = userRepository.verifyEmailByCode(
            email = verificationRequest.email,
            code = verificationRequest.code)
        ) {
            is ApiResult.Success -> call.respond(HttpStatusCode.OK, result.data)
            is ApiResult.Error -> handleError(result)
        }
    }
    post(EndPoint.SEND_CODE.path) {
        val sendCodeRequest = call.receive<SendCodeRequest>()
        val userRepository: UserService by di.instance()

        when (val result = userRepository.sendCode(sendCodeRequest)) {
            is ApiResult.Success -> call.respond(HttpStatusCode.OK, result.data)
            is ApiResult.Error -> handleError(result)
        }
    }
    post(EndPoint.RESET_PASSWORD.path) {
        val resetPasswordRequest = call.receive<ResetPasswordRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.resetPassword(
            resetPasswordRequest.email,
            resetPasswordRequest.code,
            resetPasswordRequest.password,
        )
        when (result) {
            is ApiResult.Success -> call.respond(HttpStatusCode.OK, result.data)
            is ApiResult.Error -> handleError(result)
        }
    }
    post(EndPoint.LOGIN.path) {
        val loginRequest = call.receive<LoginRequest>()
        val userRepository: UserService by di.instance()

        when (val result = userRepository.loginUser(loginRequest)) {
            is ApiResult.Success -> call.respond(HttpStatusCode.OK, result.data)
            is ApiResult.Error -> handleError(result)
        }
    }
}

private suspend fun <E : ApiError> PipelineContext<Unit, ApplicationCall>.handleError(
    result: ApiResult.Error<E>
) {
    call.respond(HttpStatusCode.fromValue(result.error.code), result.error.message)
}