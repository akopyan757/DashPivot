package com.cheesecake.common.api

sealed class ApiResult<out R, out E : ApiError> {
    data class Success<out R>(val data: R) : ApiResult<R, Nothing>()
    data class Error<E: ApiError>(val error: E) : ApiResult<Nothing, E>()
}

fun <R, T> ApiResult<R, ApiError>.fold(
    onSuccess: (R) -> T,
    onError: (ApiError) -> T
): T {
    return when (this) {
        is ApiResult.Success -> onSuccess(data)
        is ApiResult.Error -> onError(error)
    }
}

fun <R> ApiResult<R, ApiError>.fold(
    onSuccess: (R) -> Unit = {},
    onError: (ApiError) -> Unit = {}
) = fold<R, Unit>(onSuccess, onError)