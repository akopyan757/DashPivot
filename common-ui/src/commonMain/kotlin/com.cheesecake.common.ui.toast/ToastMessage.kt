package com.cheesecake.common.ui.toast

sealed interface ToastMessage {
    data object Idle: ToastMessage
    data class Info(val message: String): ToastMessage
    data class Error(val message: String): ToastMessage
}