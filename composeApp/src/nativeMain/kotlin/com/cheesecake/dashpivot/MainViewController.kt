package com.cheesecake.dashpivot

import androidx.compose.ui.window.ComposeUIViewController
import com.cheesecake.auth.feature.AuthApp

fun MainViewController(token: String?) = ComposeUIViewController {
    AuthApp()
}
