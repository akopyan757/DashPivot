package com.cheesecake.auth.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cheesecake.auth.feature.di.AndroidKoinComponent
import com.cheesecake.auth.feature.di.screenModule
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named

class AuthActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val namedSource = named("MainContent")
        AndroidKoinComponent.setAuthScope(
            getKoin().createScope("MainActivityScope", namedSource)
        )

        setContent {
            MainContent()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AndroidKoinComponent.releaseScope()
    }
}


@Composable
fun MainContent() {
    val navHostController = rememberNavController()
    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(navHostController) {
        loadKoinModules(screenModule(navHostController))
        isLoaded = true
    }

    if (isLoaded) {
        MaterialTheme {
            AuthApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MaterialTheme {
        AuthApp()
    }
}
