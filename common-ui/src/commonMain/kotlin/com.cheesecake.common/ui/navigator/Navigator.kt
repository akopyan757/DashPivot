package com.cheesecake.common.ui.navigator

import Toast

/**
 * Interface representing a navigator for screen navigation.
 */
interface Navigator: Dialog, Toast {

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
     * Navigate back to the previous screen.
     */
    fun goBack(to: RegularScreen) {}
}