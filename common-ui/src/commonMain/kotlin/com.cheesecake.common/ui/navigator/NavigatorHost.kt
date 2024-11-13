package com.cheesecake.common.ui.navigator

import androidx.compose.runtime.Composable
import com.cheesecake.common.ui.toast.ToastMessage
import kotlinx.coroutines.flow.StateFlow

interface NavigatorHost {
    val navigator: Navigator
    val toast: StateFlow<ToastMessage>
        get() = navigator.toastMessage

    @Composable
    fun Screen()
}