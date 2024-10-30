package com.cheesecake.common.ui.navigator

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class DialogNavigator: Navigator {

    private val _currentDialog = MutableStateFlow<DialogScreen?>(null)
    override val currentDialog: StateFlow<DialogScreen?> = _currentDialog

    override fun showDialog(screen: DialogScreen) {
        _currentDialog.value = screen
    }

    override fun dismissDialog() {
        _currentDialog.value = null
    }
}