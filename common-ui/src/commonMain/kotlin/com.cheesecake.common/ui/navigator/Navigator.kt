package com.cheesecake.common.ui.navigator

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface representing a navigator for screen navigation.
 */
interface Navigator {

    /**
     * Navigate to the specified regular screen.
     *
     * @param screen The regular screen to navigate to.
     */
    fun navigateTo(screen: RegularScreen) {}

    /**
     * Navigate back to the previous screen.
     */
    fun goBack() {}

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

    /**
     * Show an error message.
     */
    fun showErrorMessage(message: String) {}
}