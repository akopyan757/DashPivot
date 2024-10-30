package com.cheesecake.server.auth.route

import com.cheesecake.common.api.ApiError
import com.cheesecake.common.api.ApiResult
import com.cheesecake.common.auth.api.EndPoint
import com.cheesecake.common.auth.model.login.LoginError
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterError
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.verefication.VerificationError
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
    post(EndPoint.CONFIRM_EMAIL.path) {
        val token = call.parameters["token"]
        val userRepository: UserService by di.instance()

        when (val result = userRepository.verifyByToken(token)) {
            is ApiResult.Success -> call.respond(HttpStatusCode.Created, result.data)
            is ApiResult.Error -> handleError(result)
        }
    }
    post(EndPoint.LOGIN.path) {
        val loginRequest = call.receive<LoginRequest>()
        val userRepository: UserService by di.instance()

        when (val result = userRepository.loginUser(loginRequest)) {
            is ApiResult.Success -> call.respond(HttpStatusCode.Created, result.data)
            is ApiResult.Error -> handleError(result)
        }
    }
}

private suspend fun <E : ApiError> PipelineContext<Unit, ApplicationCall>.handleError(result: ApiResult.Error<E>) {
    when (val error = result.error) {
        RegisterError.EMAIL_TAKEN -> call.respond(HttpStatusCode.Conflict, error.message)
        RegisterError.INVALID_PASSWORD -> call.respond(HttpStatusCode.BadRequest, error.message)
        RegisterError.INVALID_EMAIL_FORMAT -> call.respond(HttpStatusCode.BadRequest, error.message)
        RegisterError.TOKEN_MISSING -> call.respond(HttpStatusCode.BadRequest, error.message)
        RegisterError.UNKNOWN -> call.respond(HttpStatusCode.InternalServerError, error.message)
        VerificationError.EMPTY_TOKEN_ERROR -> call.respond(HttpStatusCode.BadRequest, error.message)
        VerificationError.EXPIRED_TOKEN -> call.respond(HttpStatusCode.Unauthorized, error.message)
        VerificationError.UNKNOWN -> call.respond(HttpStatusCode.InternalServerError, error.message)
        LoginError.USER_NOT_FOUND -> call.respond(HttpStatusCode.NotFound, error.message)
        LoginError.INVALID_PASSWORD -> call.respond(HttpStatusCode.BadRequest, error.message)
        LoginError.EMAIL_NOT_VERIFIED -> call.respond(HttpStatusCode.Unauthorized, error.message)
        LoginError.UNKNOWN -> call.respond(HttpStatusCode.InternalServerError, error.message)
    }
}