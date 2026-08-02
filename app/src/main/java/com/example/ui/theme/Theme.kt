package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BlueSecondary,
    onPrimary = Color.White,
    primaryContainer = BluePrimaryVariant,
    onPrimaryContainer = Color.White,
    secondary = OrangePrimary,
    onSecondary = Color.White,
    secondaryContainer = OrangePrimaryVariant,
    onSecondaryContainer = Color.White,
    background = NavyDarkBg,
    onBackground = Color(0xFFF1F5F9),
    surface = NavySurfaceDark,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFE2E8F0),
    outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueLightContainer,
    onPrimaryContainer = BluePrimaryVariant,
    secondary = OrangePrimary,
    onSecondary = Color.White,
    secondaryContainer = OrangeLightContainer,
    onSecondaryContainer = OrangePrimaryVariant,
    background = LightBackground,
    onBackground = TextPrimary,
    surface = LightSurface,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondary,
    outline = SlateBorder
)

@Composable
fun MistriConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
