package com.wealth.sampatika.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    secondary = RoyalBlueDark,
    background = DarkBlue
)

private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    secondary = RoyalBlueDark,
    background = White
)

@Composable
fun SampatikaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}