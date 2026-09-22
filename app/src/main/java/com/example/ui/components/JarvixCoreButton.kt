package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.JarvixCoreState

@Composable
fun JarvixCoreButton(
    coreState: JarvixCoreState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "core_anim")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val primaryColor = when (coreState) {
        JarvixCoreState.LISTENING -> Color(0xFF00FF66)
        JarvixCoreState.PROCESSING -> Color(0xFF00F0FF)
        JarvixCoreState.SPEAKING -> Color(0xFF00A8FF)
        JarvixCoreState.SLEEP -> Color(0xFF334155)
        JarvixCoreState.IDLE -> Color(0xFF00FFFF)
    }

    Box(
        modifier = modifier
            .size(160.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = center
            val radius = size.minDimension / 2f - 8.dp.toPx()

            // Outer rotating HUD ring
            drawCircle(
                color = primaryColor.copy(alpha = 0.4f),
                radius = radius,
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )

            // Inner glowing core background
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(primaryColor.copy(alpha = 0.6f), Color(0xFF030712)),
                    center = center,
                    radius = radius
                ),
                radius = radius * 0.85f,
                center = center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "JARVIX",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (coreState) {
                    JarvixCoreState.IDLE -> "ONLINE"
                    JarvixCoreState.LISTENING -> "LISTENING"
                    JarvixCoreState.PROCESSING -> "PROCESSING"
                    JarvixCoreState.SPEAKING -> "SPEAKING"
                    JarvixCoreState.SLEEP -> "SLEEP"
                },
                color = primaryColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }
    }
}
