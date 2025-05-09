package com.cheesecake.common.api

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

class RequestHandler {

    suspend fun <T, E : ApiError> execute(
        request: suspend () -> HttpResponse,
        onSuccess: suspend (body: String) -> T,
        onError: suspend (code: HttpStatusCode, body: String) -> E,
        onException: suspend (exception: Exception) -> E
    ): ApiResult<T, E> {
        return try {
            val httpResponse = request()
            val statusCode = httpResponse.status.value
            val method = httpResponse.request.method.value
            val path = httpResponse.request.url.encodedPathAndQuery
            if (HttpStatusCode.fromValue(statusCode).isSuccess()) {
                val body = httpResponse.bodyAsText()
                Log.info(TAG, "Response: Success: method=$method, path=$path, body=$body")
                ApiResult.Success(onSuccess(body))
            } else {
                httpResponse.bodyAsText()
                Log.debug(TAG, "Response Error: method=$method, path=$path, code=$statusCode")
                ApiResult.Error(onError(httpResponse.status, httpResponse.bodyAsText()))
            }
        } catch (e: Exception) {
            Log.error(TAG, e)
            ApiResult.Error(onException(e))
        }
    }

    companion object {
        private const val TAG = "RequestHandler"
    }
}