package com.nhapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00A884),
    secondary = Color(0xFF1F2C34),
    background = Color(0xFF111B21),
    surface = Color(0xFF1F2C34),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00A884),
    secondary = Color(0xFF25D366),
    background = Color.White,
    surface = Color(0xFFF0F2F5),
)

@Composable
fun NHAPPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
