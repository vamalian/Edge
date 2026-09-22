package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.JarvixAvatar3D
import com.example.ui.components.JarvixCoreButton
import com.example.ui.components.HudCard
import com.example.viewmodel.JarvixCoreState
import com.example.viewmodel.JarvixViewModel

@Composable
fun MainCommandCenterScreen(
    viewModel: JarvixViewModel,
    onNavigate: (String) -> Unit
) {
    val coreState by viewModel.coreState.collectAsState()
    val battery by viewModel.batteryInfo.collectAsState()
    val network by viewModel.networkInfo.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF030712), Color(0xFF0A192F))
                )
            )
    ) {
        // Background 3D Holographic Character Avatar
        JarvixAvatar3D(
            coreState = coreState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 120.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header HUD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "JARVIX",
                        color = Color(0xFF00FFFF),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp
                    )
                    Text(
                        text = if (coreState == JarvixCoreState.SLEEP) "SLEEP MODE" else "SYSTEMS ONLINE",
                        color = if (coreState == JarvixCoreState.SLEEP) Color(0xFF8892B0) else Color(0xFF00FF66),
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = { onNavigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color(0xFF00FFFF))
                    }
                    IconButton(onClick = { onNavigate("owner_auth") }) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = "Owner Dashboard", tint = Color(0xFF00FFFF))
                    }
                }
            }

            // Center Interactive Core Button & Status
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                HudCard(modifier = Modifier.widthIn(max = 300.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "BAT: $battery", color = Color(0xFF8892B0), fontSize = 12.sp)
                        Text(text = "NET: $network", color = Color(0xFF8892B0), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                JarvixCoreButton(
                    coreState = coreState,
                    onClick = {
                        if (coreState == JarvixCoreState.SLEEP) {
                            viewModel.setCoreState(JarvixCoreState.IDLE)
                            viewModel.voiceManager.speak("Systems online. I'm listening.", viewModel.soundEnabled.value)
                        } else {
                            viewModel.setCoreState(JarvixCoreState.LISTENING)
                            viewModel.sendPrompt("Hello Jarvix, status report.")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = when (coreState) {
                        JarvixCoreState.IDLE -> "TAP CORE TO ACTIVATE"
                        JarvixCoreState.LISTENING -> "JARVIX IS LISTENING..."
                        JarvixCoreState.PROCESSING -> "PROCESSING NEURAL QUERY..."
                        JarvixCoreState.SPEAKING -> "JARVIX SPEAKING..."
                        JarvixCoreState.SLEEP -> "TAP CORE TO WAKE UP"
                    },
                    color = Color(0xFF00FFFF).copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    letterSpacing = 2.sp
                )
            }

            // Bottom Command Center Menu Bar
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F).copy(alpha = 0.9f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CommandMenuIcon(Icons.Default.Chat, "Chat") { onNavigate("chat") }
                    CommandMenuIcon(Icons.Default.Mic, "Voice") { onNavigate("voice") }
                    CommandMenuIcon(Icons.Default.Devices, "Controls") { onNavigate("controls") }
                    CommandMenuIcon(Icons.Default.MonitorHeart, "Status") { onNavigate("status") }
                    CommandMenuIcon(Icons.Default.Info, "About") { onNavigate("about") }
                }
            }
        }
    }
}

@Composable
fun CommandMenuIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = label, tint = Color(0xFF00FFFF), modifier = Modifier.size(24.dp))
        }
    }
}
