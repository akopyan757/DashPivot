package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.api.ApiResponse
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
        val result = userRepository.registerUser(registerRequest)
        handleResult(result)
    }
    post(EndPoint.REGISTER_CONFIRM.path) {
        val verificationRequest = call.receive<VerificationRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.verifyEmailByCode(
            email = verificationRequest.email,
            code = verificationRequest.code,
        )
        handleResult(result)
    }
    post(EndPoint.SEND_CODE.path) {
        val sendCodeRequest = call.receive<SendCodeRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.sendCode(sendCodeRequest)
        handleResult(result)
    }
    post(EndPoint.RESET_PASSWORD.path) {
        val resetPasswordRequest = call.receive<ResetPasswordRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.resetPassword(
            resetPasswordRequest.email,
            resetPasswordRequest.code,
            resetPasswordRequest.password,
        )
        handleResult(result)
    }
    post(EndPoint.LOGIN.path) {
        val loginRequest = call.receive<LoginRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.loginUser(loginRequest)
        handleResult(result)
    }
}

private suspend fun <T> PipelineContext<Unit, ApplicationCall>.handleResult(
    result: ApiResult<T, ApiError>
) {
    when (result) {
        is ApiResult.Success -> handleSuccess(result)
        is ApiResult.Error -> handleError(result)
    }
}

private suspend fun <T> PipelineContext<Unit, ApplicationCall>.handleSuccess(
    result: ApiResult.Success<T>
) {
    call.respond(HttpStatusCode.OK, ApiResponse(
        code = HttpStatusCode.OK.value,
        message = HttpStatusCode.OK.description,
        data = result.data,
    ))
}

private suspend fun <E : ApiError> PipelineContext<Unit, ApplicationCall>.handleError(
    result: ApiResult.Error<E>
) {
    call.respond(HttpStatusCode.fromValue(result.error.code), ApiResponse<Nothing>(
        code = result.error.code,
        message = result.error.message,
    ))
}