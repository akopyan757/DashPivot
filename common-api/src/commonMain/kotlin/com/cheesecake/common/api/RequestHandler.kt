package com.cheesecake.common.api

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class RequestHandler {

    suspend fun <T, E : ApiError> execute(
        request: suspend () -> HttpResponse,
        serializer: KSerializer<T>,
        onSuccess: suspend (body: T) -> T = { it },
        onError: suspend (code: Int, body: String) -> E,
        onException: suspend (exception: Exception) -> E
    ): ApiResult<T, E> {
        return try {
            val httpResponse = request()
            val statusCode = httpResponse.status.value
            val method = httpResponse.request.method.value
            val path = httpResponse.request.url.encodedPathAndQuery
            val body = httpResponse.bodyAsText().let { json ->
                Json.decodeFromString(ApiResponseSerializer(serializer), json)
            }
            if (HttpStatusCode.fromValue(statusCode).isSuccess() && body.code == 200 && body.data != null) {
                Log.info(TAG, "Response: Success: method=$method, path=$path, body=$body")
                ApiResult.Success(onSuccess(body.data))
            } else {
                httpResponse.bodyAsText()
                Log.debug(TAG, "Response Error: method=$method, path=$path, code=$statusCode")
                ApiResult.Error(onError(body.code, body.message))
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