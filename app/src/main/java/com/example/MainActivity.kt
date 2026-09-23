package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.settings.SettingsScreen
import com.example.ui.settings.SettingsViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install modern Core Splashscreen layout before super.onCreate
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val preferences by viewModel.preferences.collectAsState()
            val isEnabled by viewModel.isKeyboardEnabled.collectAsState()
            val isDefault by viewModel.isKeyboardDefault.collectAsState()

            MyApplicationTheme {
                SettingsScreen(
                    preferences = preferences,
                    isKeyboardEnabled = isEnabled,
                    isKeyboardDefault = isDefault,
                    onRefreshStatus = { viewModel.checkKeyboardStatus() },
                    onThemeSelect = { viewModel.setTheme(it) },
                    onSoundToggle = { viewModel.setSoundEnabled(it) },
                    onHapticToggle = { viewModel.setHapticEnabled(it) },
                    onPopupToggle = { viewModel.setPopupEnabled(it) },
                    onHeightSelect = { viewModel.setHeight(it) }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkKeyboardStatus()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
