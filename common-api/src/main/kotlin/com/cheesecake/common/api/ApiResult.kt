package com.cheesecake.common.api

sealed class ApiResult<out R, out E> {
    data class Success<out R>(val data: R) : ApiResult<R, Nothing>()
    data class Error<E: ApiError>(val error: E) : ApiResult<Nothing, E>()
}