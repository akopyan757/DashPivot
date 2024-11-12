package com.cheesecake.auth.data.service

import com.cheesecake.common.api.BASE_URL
import com.cheesecake.common.auth.api.EndPoint
import com.cheesecake.common.auth.model.login.LoginRequest
import com.cheesecake.common.auth.model.registration.RegisterRequest
import com.cheesecake.common.auth.model.resendCode.ResendCodeRequest
import com.cheesecake.common.auth.model.verefication.VerificationRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiServiceImpl(private val client: HttpClient) : ApiService {

    override suspend fun registerUser(request: RegisterRequest): HttpResponse {
        return client.post {
            url("$BASE_URL${EndPoint.REGISTER.path}")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun verificationCode(email: String, code: String): HttpResponse {
        return client.post {
            url("$BASE_URL${EndPoint.CONFIRM_EMAIL_BY_CODE.path}")
            contentType(ContentType.Application.Json)
            setBody(VerificationRequest(email, code))
        }
    }

    override suspend fun resendCode(email: String): HttpResponse {
        return client.post {
            url("$BASE_URL${EndPoint.RESEND_CODE.path}")
            contentType(ContentType.Application.Json)
            setBody(ResendCodeRequest(email))
        }
    }

    override suspend fun loginUser(request: LoginRequest): HttpResponse {
        return client.post {
            url("$BASE_URL${EndPoint.LOGIN.path}")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}
