package com.cheesecake.common.ui.navigator

interface Screen {
    val storyBoardName: String
    val route: String
    val prefix: String
    val fullRoute: String get() = prefix + route
}

/**
 * Interface representing a regular screen.
 */
interface RegularScreen : Screen {
    override val prefix: String
        get() = PREFIX

    companion object {
        const val PREFIX = "regular_"
    }
}

/**
 * Interface representing an overlay screen.
 */
interface DialogScreen : Screen {
    override val prefix: String
        get() = PREFIX

    companion object {
        const val PREFIX = "dialog_"
    }
}