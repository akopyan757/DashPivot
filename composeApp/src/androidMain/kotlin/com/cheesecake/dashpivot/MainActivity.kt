package com.cheesecake.dashpivot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cheesecake.dashpivot.entry.AppEntry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appEntry = AppEntry(token = handleUri())

        setContent {
            App(appEntry)
        }
    }

    private fun handleUri(): String? {
        return intent?.data?.let { uri ->
            if (uri.scheme == "dashpivot" && uri.host == "verify") {
                uri.getQueryParameter("token")
            } else null
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
