package com.cheesecake.common.ui.dialog

import androidx.compose.runtime.Composable

interface DialogController {
    fun showDialog(
        backgroundColor: Int? = null,
        backgroundOpacity: Float = 1.0f,
        isCancelableOnTouchOutside: Boolean = true,
        content: @Composable (() -> Unit)
    )
}