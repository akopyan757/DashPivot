package com.cheesecake.server.auth.route.mail

import com.cheesecake.common.auth.model.sendCode.SendCodeType

interface IEmailService {
    fun sendVerificationEmail(email: String, code: String, sendCodeType: SendCodeType): Boolean
}