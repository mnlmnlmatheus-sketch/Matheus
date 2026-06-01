package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Disable wallpaper sync to favor user customization colors
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) {
    darkColorScheme(
      primary = Lime500,
      secondary = Lime400,
      tertiary = Amber400,
      background = Slate900,
      surface = Slate800,
      onPrimary = Slate900,
      onSecondary = Slate900,
      onBackground = Slate50,
      onSurface = Slate50,
      outline = Slate600
    )
  } else {
    lightColorScheme(
      primary = Lime500,
      secondary = Slate800,
      tertiary = Amber400,
      background = Slate50,
      surface = Color.White,
      onPrimary = Color.White,
      onSecondary = Color.White,
      onBackground = Slate900,
      onSurface = Slate900,
      outline = Slate400
    )
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
