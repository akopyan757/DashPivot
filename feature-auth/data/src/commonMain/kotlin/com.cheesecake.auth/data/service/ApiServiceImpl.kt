package com.cheesecake.auth.data.service

import com.cheesecake.common.api.BASE_URL
import com.cheesecake.common.auth.api.EndPoint
import com.cheesecake.common.auth.model.RegisterRequest
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
}
