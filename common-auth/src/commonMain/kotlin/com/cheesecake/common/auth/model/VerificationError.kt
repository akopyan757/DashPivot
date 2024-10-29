package com.cheesecake.common.auth.model

import com.cheesecake.common.api.ApiError

object VerificationError: ApiError {
    override val message: String = "Verification failed. Please try again."
}