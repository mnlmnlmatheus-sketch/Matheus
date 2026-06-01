package com.example

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ThemeConfig
import com.example.ui.WorkoutAppMainScreen
import com.example.ui.WorkoutViewModel
import com.example.data.WorkoutPreferences

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    // Initialize Theme Config and Refresh rate options upon start
    val prefs = WorkoutPreferences(this)
    val savedTheme = prefs.getSelectedTheme()
    val paletteObj = ThemeConfig.palettes.find { it.code == savedTheme }
    if (paletteObj != null) {
      ThemeConfig.primaryColor = paletteObj.primary
      ThemeConfig.secondaryColor = paletteObj.secondary
    }

    // Force maximum frame rate (refresh rate) of the device if enabled (skipped on emulators)
    if (prefs.getHighRefreshEnabled() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isEmulator()) {
        try {
            val display = this.display
            val modes = display?.supportedModes
            val maxRateMode = modes?.maxByOrNull { it.refreshRate }
            if (maxRateMode != null) {
                val layoutParams = window.attributes
                layoutParams.preferredDisplayModeId = maxRateMode.modeId
                window.attributes = layoutParams
            }
        } catch (e: Exception) {
            // Ignore if display modes unsupported
        }
    }

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
          val viewModel: WorkoutViewModel = viewModel()
          WorkoutAppMainScreen(viewModel = viewModel)
        }
      }
    }
  }

  private fun isEmulator(): Boolean {
    val model = Build.MODEL
    val brand = Build.BRAND
    val device = Build.DEVICE
    val product = Build.PRODUCT
    val hardware = Build.HARDWARE
    val fingerprint = Build.FINGERPRINT
    return brand.startsWith("generic") ||
           fingerprint.startsWith("generic") ||
           fingerprint.startsWith("unknown") ||
           hardware.contains("goldfish") ||
           hardware.contains("ranchu") ||
           model.contains("google_sdk") ||
           model.contains("Emulator") ||
           model.contains("Android SDK build") ||
           product.contains("sdk_gphone") ||
           product.contains("google_sdk") ||
           product.contains("sdk") ||
           product.contains("sdk_x86") ||
           product.contains("vbox86p") ||
           product.contains("emulator") ||
           product.contains("simulator")
  }
}
