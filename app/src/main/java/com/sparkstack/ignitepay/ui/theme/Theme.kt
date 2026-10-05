package com.sparkstack.ignitepay.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val IgniteColors = darkColorScheme(
    primary = Color(0xFFFF6B2C),
    secondary = Color(0xFF7C5CFC),
    background = Color(0xFF080A0F),
    surface = Color(0xFF11141A),
    surfaceVariant = Color(0xFF1A1F27),
    onPrimary = Color(0xFF080A0F),
    onBackground = Color(0xFFF7F8FA),
    onSurface = Color(0xFFF7F8FA)
)

@Composable
fun IgnitePayTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = IgniteColors, content = content)
}
