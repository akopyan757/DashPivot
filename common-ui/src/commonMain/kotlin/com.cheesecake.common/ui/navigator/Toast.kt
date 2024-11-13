import com.cheesecake.common.ui.toast.ToastMessage
import kotlinx.coroutines.flow.StateFlow


interface Toast {
    /**
     * The current regular screen.
     */
    val toastMessage: StateFlow<ToastMessage>

    /**
     * Show an error message.
     */
    fun showToastMessage(toast: ToastMessage) {}

    /**
     * Dismiss the error message.
     */
    fun dismissToastMessage() {}
}