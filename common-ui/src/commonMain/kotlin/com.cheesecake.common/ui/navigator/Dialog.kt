package com.cheesecake.common.ui.navigator

import kotlinx.coroutines.flow.StateFlow

interface Dialog {

    /**
     * The current dialog screen.
     */
    val currentDialog: StateFlow<DialogScreen?>

    /**
     * Show a dialog screen.
     */
    fun showDialog(screen: DialogScreen) {}

    /**
     * Dismiss the dialog screen.
     */
    fun dismissDialog() {}
}