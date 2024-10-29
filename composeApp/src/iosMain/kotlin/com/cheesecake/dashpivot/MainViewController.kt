package com.cheesecake.dashpivot

import androidx.compose.ui.window.ComposeUIViewController
import com.cheesecake.dashpivot.entry.AppEntry

fun MainViewController(token: String?) = ComposeUIViewController {
    println("MainViewController: Updated token in Compose: $token")
    App(appEntry = AppEntry(token = token))
}