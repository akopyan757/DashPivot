package com.cheesecake.dashpivot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.cheesecake.auth.feature.di.screenModule
import com.cheesecake.auth.feature.events.VerifyEmailEvent
import com.cheesecake.common.ui.events.EventStateHolder
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

class MainActivity : ComponentActivity(), KoinComponent {

    private val eventStateHolder: EventStateHolder<VerifyEmailEvent> by inject()
    private lateinit var activityScope: Scope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val namedSource = named("MainContent")
        activityScope = getKoin().createScope("MainActivityScope", namedSource)

        handleVerificationUri()?.also { token ->
            println("AndroidNavigatorHost: handleVerificationUri = $token")
            lifecycleScope.launch {
                eventStateHolder.emitEvent(VerifyEmailEvent(token))
            }
        }

        setContent {
            MainContent()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.close()
    }

    private fun handleVerificationUri(): String? {
        return intent?.data?.let { uri ->
            if (uri.scheme == "dashpivot" && uri.host == "verify") {
                uri.getQueryParameter("token")
            } else null
        }
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
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MaterialTheme {
        App()
    }
}
