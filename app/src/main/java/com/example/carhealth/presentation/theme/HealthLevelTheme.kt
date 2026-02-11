package com.example.carhealth.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightScheme = lightColorScheme(
    primary = Color(0xFF0F9E71),
    onPrimary = Color(0xFFEDF9F3),
    primaryContainer = Color(0xFFD6F2E5),
    onPrimaryContainer = Color(0xFF063422),
    secondary = Color(0xFF2E6F5A),
    onSecondary = Color(0xFFF0FAF5),
    background = Color(0xFFF2F6F3),
    onBackground = Color(0xFF101914),
    surface = Color(0xFFFBFDFC),
    onSurface = Color(0xFF15201A),
    surfaceVariant = Color(0xFFE5EEE8),
    onSurfaceVariant = Color(0xFF44534B),
    outline = Color(0xFFC2D0C7),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF)
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF4CD6A2),
    onPrimary = Color(0xFF002114),
    primaryContainer = Color(0xFF0A3B29),
    onPrimaryContainer = Color(0xFFB9F4DA),
    secondary = Color(0xFF8DCDB4),
    onSecondary = Color(0xFF0D3324),
    background = Color(0xFF060C09),
    onBackground = Color(0xFFE6F2EB),
    surface = Color(0xFF0D1512),
    onSurface = Color(0xFFE6F2EB),
    surfaceVariant = Color(0xFF18221D),
    onSurfaceVariant = Color(0xFFB7C8BD),
    outline = Color(0xFF33413A),
    error = Color(0xFFFF8A80),
    onError = Color(0xFF380202)
)

private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 38.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.4).sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 23.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp
    )
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp)
)

@Composable
fun HealthLevelTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
