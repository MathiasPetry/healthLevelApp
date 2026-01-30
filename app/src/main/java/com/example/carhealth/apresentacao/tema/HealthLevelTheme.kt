package com.example.carhealth.apresentacao.tema

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Preto = Color(0xFF000000)
private val CinzaEscuro = Color(0xFF1F2933)
private val Branco = Color(0xFFF9FAFB)
private val VerdeDestaque = Color(0xFF10B981)
private val VermelhoErro = Color(0xFFDC2626)

private val DarkScheme = darkColorScheme(
    primary = VerdeDestaque,
    onPrimary = Preto,
    background = Preto,
    onBackground = Branco,
    surface = CinzaEscuro,
    onSurface = Branco,
    error = VermelhoErro,
    onError = Branco,
    secondary = VerdeDestaque,
    onSecondary = Preto
)

private val LightScheme = lightColorScheme(
    primary = VerdeDestaque,
    onPrimary = Preto,
    background = Branco,
    onBackground = Preto,
    surface = Branco,
    onSurface = Preto,
    error = VermelhoErro,
    onError = Branco,
    secondary = VerdeDestaque,
    onSecondary = Preto
)

private val Typography = androidx.compose.material3.Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)

@Composable
fun HealthLevelTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val scheme = if (useDarkTheme) DarkScheme else LightScheme
    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        content = content
    )
}
