package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val JarvixDarkColorScheme =
  darkColorScheme(
    primary = JarvixCyan,
    secondary = JarvixBlue,
    tertiary = JarvixGreen,
    background = JarvixDark,
    surface = JarvixSurface,
    surfaceVariant = JarvixSurfaceVariant,
    onPrimary = JarvixDark,
    onSecondary = JarvixDark,
    onBackground = JarvixText,
    onSurface = JarvixText,
    onSurfaceVariant = JarvixTextDim,
    error = JarvixRed
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // JARVIX is default futuristic dark mode
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = JarvixDarkColorScheme,
    typography = Typography,
    content = content
  )
}
