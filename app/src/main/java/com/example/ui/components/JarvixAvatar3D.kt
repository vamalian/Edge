package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.viewmodel.JarvixCoreState
import kotlin.math.sin

@Composable
fun JarvixAvatar3D(coreState: JarvixCoreState, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_anim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val color = when (coreState) {
        JarvixCoreState.LISTENING -> Color(0xFF00FF66)
        JarvixCoreState.PROCESSING -> Color(0xFF00F0FF)
        JarvixCoreState.SPEAKING -> Color(0xFF00A8FF)
        JarvixCoreState.SLEEP -> Color(0xFF334155)
        JarvixCoreState.IDLE -> Color(0xFF00F0FF)
    }

    Canvas(modifier = modifier) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val radius = size.minDimension * 0.35f * pulse

        // Outer holographic HUD rings
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color.copy(alpha = 0.3f), Color.Transparent),
                center = Offset(centerX, centerY),
                radius = radius * 1.8f
            ),
            radius = radius * 1.8f,
            center = Offset(centerX, centerY)
        )

        // Holographic wireframe avatar silhouette representation (Futuristic Sci-Fi HUD representation of JARVIX AI)
        drawCircle(
            color = color.copy(alpha = 0.7f),
            radius = radius,
            center = Offset(centerX, centerY - radius * 0.2f),
            style = Stroke(width = 3.dp.toPx())
        )

        // Eyes glow
        val eyeOffset = radius * 0.25f
        drawCircle(
            color = Color.White,
            radius = 6.dp.toPx(),
            center = Offset(centerX - eyeOffset, centerY - radius * 0.3f)
        )
        drawCircle(
            color = Color.White,
            radius = 6.dp.toPx(),
            center = Offset(centerX + eyeOffset, centerY - radius * 0.3f)
        )

        // Core chest emblem
        drawCircle(
            color = color,
            radius = radius * 0.25f,
            center = Offset(centerX, centerY + radius * 0.5f)
        )
    }
}
