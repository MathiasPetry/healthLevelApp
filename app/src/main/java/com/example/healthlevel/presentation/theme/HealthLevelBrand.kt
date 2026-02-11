package com.example.healthlevel.presentation.theme

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun HealthLevelEmblem(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val isDarkTheme = colors.background.luminance() < 0.5f
    val accent = if (isDarkTheme) colors.primary else Color(0xFF135E43)
    val accentSoft = if (isDarkTheme) colors.secondary else Color(0xFF2A8A67)
    val strokeColor = if (isDarkTheme) colors.onBackground else Color(0xFF121212)
    val lineScale = if (isDarkTheme) 1.1f else 1f

    Canvas(modifier = modifier) {
        val minSide = size.minDimension
        val centerX = size.width * 0.48f
        val centerY = size.height * 0.48f

        val ringSize = minSide * 0.8f
        val ringTopLeft = Offset(centerX - ringSize / 2f, centerY - ringSize / 2f)
        val gaugeStroke = minSide * 0.085f * lineScale
        val baseStroke = minSide * 0.035f * lineScale

        drawArc(
            color = accent,
            startAngle = 194f,
            sweepAngle = 162f,
            useCenter = false,
            topLeft = ringTopLeft,
            size = androidx.compose.ui.geometry.Size(ringSize, ringSize),
            style = Stroke(width = gaugeStroke, cap = StrokeCap.Round)
        )

        drawArc(
            color = accentSoft.copy(alpha = if (isDarkTheme) 0.88f else 0.72f),
            startAngle = 334f,
            sweepAngle = 112f,
            useCenter = false,
            topLeft = Offset(size.width * 0.52f, size.height * 0.44f),
            size = androidx.compose.ui.geometry.Size(minSide * 0.44f, minSide * 0.44f),
            style = Stroke(width = minSide * 0.048f * lineScale, cap = StrokeCap.Round)
        )

        val tickAngles = listOf(208f, 234f, 258f, 282f)
        tickAngles.forEach { angle ->
            val radians = Math.toRadians(angle.toDouble())
            val outer = Offset(
                x = centerX + kotlin.math.cos(radians).toFloat() * (ringSize * 0.38f),
                y = centerY + kotlin.math.sin(radians).toFloat() * (ringSize * 0.38f)
            )
            val inner = Offset(
                x = centerX + kotlin.math.cos(radians).toFloat() * (ringSize * 0.3f),
                y = centerY + kotlin.math.sin(radians).toFloat() * (ringSize * 0.3f)
            )
            drawLine(
                color = accent.copy(alpha = if (isDarkTheme) 0.95f else 0.86f),
                start = outer,
                end = inner,
                strokeWidth = minSide * 0.032f * lineScale,
                cap = StrokeCap.Round
            )
        }

        drawCircle(
            color = strokeColor,
            radius = minSide * 0.083f,
            center = Offset(size.width * 0.5f, size.height * 0.35f),
            style = Stroke(width = baseStroke)
        )

        val shoulder = Path().apply {
            moveTo(size.width * 0.3f, size.height * 0.55f)
            cubicTo(
                size.width * 0.38f,
                size.height * 0.42f,
                size.width * 0.49f,
                size.height * 0.43f,
                size.width * 0.58f,
                size.height * 0.5f
            )
        }
        drawPath(
            path = shoulder,
            color = strokeColor,
            style = Stroke(width = baseStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        val torso = Path().apply {
            moveTo(size.width * 0.52f, size.height * 0.56f)
            cubicTo(
                size.width * 0.47f,
                size.height * 0.64f,
                size.width * 0.46f,
                size.height * 0.73f,
                size.width * 0.46f,
                size.height * 0.82f
            )
        }
        drawPath(
            path = torso,
            color = strokeColor,
            style = Stroke(width = baseStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        val lowerBody = Path().apply {
            moveTo(size.width * 0.57f, size.height * 0.58f)
            cubicTo(
                size.width * 0.61f,
                size.height * 0.72f,
                size.width * 0.62f,
                size.height * 0.8f,
                size.width * 0.7f,
                size.height * 0.8f
            )
        }
        drawPath(
            path = lowerBody,
            color = strokeColor,
            style = Stroke(width = baseStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        val arrow = Path().apply {
            moveTo(size.width * 0.23f, size.height * 0.58f)
            cubicTo(
                size.width * 0.42f,
                size.height * 0.55f,
                size.width * 0.67f,
                size.height * 0.47f,
                size.width * 0.84f,
                size.height * 0.22f
            )
        }
        drawPath(
            path = arrow,
            color = strokeColor,
            style = Stroke(width = baseStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        val arrowHead = Path().apply {
            moveTo(size.width * 0.84f, size.height * 0.22f)
            lineTo(size.width * 0.79f, size.height * 0.26f)
            moveTo(size.width * 0.84f, size.height * 0.22f)
            lineTo(size.width * 0.82f, size.height * 0.29f)
        }
        drawPath(
            path = arrowHead,
            color = strokeColor,
            style = Stroke(width = baseStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun HealthLevelWordmark(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val isDarkTheme = colors.background.luminance() < 0.5f
    val healthColor = if (isDarkTheme) colors.primary else Color(0xFF135E43)
    val levelColor = if (isDarkTheme) colors.onBackground else Color(0xFF101010)

    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = healthColor)) {
                append("Health")
            }
            withStyle(style = SpanStyle(color = levelColor)) {
                append("Level")
            }
        },
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}
