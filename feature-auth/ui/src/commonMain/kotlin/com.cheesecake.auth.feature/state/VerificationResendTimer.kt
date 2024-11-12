package com.cheesecake.auth.feature.state

import com.cheesecake.common.auth.config.Config
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlinx.serialization.Serializable

const val RESEND_KEY = "resend_timer"

@Serializable
data class VerificationResendTimer(
    val email: String,
    val resendAvailableAt: Long,
) {
    fun canResend(): Boolean {
        return resendAvailableAt < Clock.System.now().toEpochMilliseconds()
    }

    fun calculateRestSeconds(): Int {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        return ((resendAvailableAt - currentTime) / 1000).toInt()
    }

    companion object {

        fun init(email: String): VerificationResendTimer {
            val currentTime = Clock.System.now()
            val resendAvailableAt = currentTime.plus(Config.VERIFICATION_CODE_SENDING_DELAY_SEC, DateTimeUnit.SECOND)
            return VerificationResendTimer(email, resendAvailableAt.toEpochMilliseconds())
        }
    }
}
