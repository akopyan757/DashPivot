package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.api.ApiResponse
import com.cheesecake.common.api.ApiResponseSerializer
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.api.EndPoint
import com.cheesecake.common.auth.model.changePassword.ResetPasswordRequest
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.registration.RegisterResponse
import com.cheesecake.common.auth.model.sendCode.SendCodeRequest
import com.cheesecake.common.auth.model.verefication.VerificationRequest
import com.cheesecake.common.auth.service.UserService
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.instance


fun Route.authRoute(di: DI) {
    post(EndPoint.REGISTER.path) {
        val registerRequest = call.receive<RegisterRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.registerUser(registerRequest)
        handleResult(result, RegisterResponse.serializer())
    }
    post(EndPoint.REGISTER_CONFIRM.path) {
        val verificationRequest = call.receive<VerificationRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.verifyEmailByCode(verificationRequest)
        handleResult(result, String.serializer())
    }
    post(EndPoint.SEND_CODE.path) {
        val sendCodeRequest = call.receive<SendCodeRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.sendCode(sendCodeRequest)
        handleResult(result, String.serializer())
    }
    post(EndPoint.RESET_PASSWORD.path) {
        val resetPasswordRequest = call.receive<ResetPasswordRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.resetPassword(resetPasswordRequest)
        handleResult(result, String.serializer())
    }
    post(EndPoint.LOGIN.path) {
        val loginRequest = call.receive<LoginRequest>()
        val userRepository: UserService by di.instance()
        val result = userRepository.loginUser(loginRequest)
        handleResult(result, String.serializer())
    }
}

private suspend fun <T> PipelineContext<Unit, ApplicationCall>.handleResult(
    result: ApiResult<T, ApiError>,
    serializer: KSerializer<T>,
) {
    when (result) {
        is ApiResult.Success -> handleSuccess(result, serializer)
        is ApiResult.Error -> handleError(result)
    }
}

private suspend fun <T> PipelineContext<Unit, ApplicationCall>.handleSuccess(
    result: ApiResult.Success<T>,
    serializer: KSerializer<T>,
) {
    val response = ApiResponse(
        code = HttpStatusCode.OK.value,
        message = HttpStatusCode.OK.description,
        data = result.data,
    )
    call.respondText(
        Json.encodeToString(ApiResponseSerializer(serializer), response),
        ContentType.Application.Json
    )
}

private suspend fun <E : ApiError> PipelineContext<Unit, ApplicationCall>.handleError(
    result: ApiResult.Error<E>
) {
    val typeInfo = typeInfo<ApiResponse<Nothing>>()
    call.respond(HttpStatusCode.fromValue(result.error.code), ApiResponse<Nothing>(
        code = result.error.code,
        message = result.error.message,
    ), typeInfo)
}