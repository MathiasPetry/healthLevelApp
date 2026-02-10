package com.example.carhealth.apresentacao.tema

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun PremiumBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val brush = Brush.verticalGradient(
        colors = listOf(
            colors.background,
            colors.surfaceVariant.copy(alpha = 0.7f),
            colors.background
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = brush),
        content = content
    )
}

@Composable
fun PremiumPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(24.dp)

    Surface(
        modifier = modifier
            .shadow(
                elevation = 14.dp,
                shape = shape,
                ambientColor = colors.primary.copy(alpha = 0.14f),
                spotColor = colors.primary.copy(alpha = 0.2f)
            )
            .border(
                border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.35f)),
                shape = shape
            ),
        shape = shape,
        color = colors.surface.copy(alpha = 0.94f),
        tonalElevation = 2.dp,
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

@Composable
fun premiumFieldColors(): TextFieldColors {
    val colors = MaterialTheme.colorScheme
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colors.primary,
        unfocusedBorderColor = colors.outline.copy(alpha = 0.65f),
        focusedLabelColor = colors.primary,
        unfocusedLabelColor = colors.onSurfaceVariant,
        cursorColor = colors.primary,
        focusedTextColor = colors.onSurface,
        unfocusedTextColor = colors.onSurface,
        focusedContainerColor = colors.surface.copy(alpha = 0.48f),
        unfocusedContainerColor = colors.surface.copy(alpha = 0.3f)
    )
}
