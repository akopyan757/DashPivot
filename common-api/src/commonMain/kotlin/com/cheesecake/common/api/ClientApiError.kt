package com.cheesecake.common.api

class ClientApiError(
    exception: Exception
) : ApiError{
    override val code: Int = 500
    override val message: String = exception.message.orEmpty()
}