package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.JarvixViewModel
import com.example.viewmodel.StartupStage

@Composable
fun StartupScreen(viewModel: JarvixViewModel, onBootComplete: () -> Unit) {
    val stage by viewModel.startupStage.collectAsState()

    LaunchedEffect(stage) {
        if (stage == StartupStage.ONLINE) {
            onBootComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030712)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "JARVIX",
                color = Color(0xFF00FFFF),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 6.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                color = Color(0xFF00FFFF),
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = when (stage) {
                    StartupStage.BOOTING -> "BOOTING SYSTEM..."
                    StartupStage.INITIALIZING -> "INITIALIZING JARVIX..."
                    StartupStage.SYSTEM_CHECK -> "SYSTEM CHECK..."
                    StartupStage.AI_READY -> "AI CORE ONLINE..."
                    StartupStage.VOICE_READY -> "VOICE SYSTEM READY..."
                    StartupStage.ONLINE -> "JARVIX ONLINE"
                },
                color = Color(0xFF8892B0),
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )
        }
    }
}
