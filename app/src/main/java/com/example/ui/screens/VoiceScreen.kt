package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.JarvixCoreState
import com.example.viewmodel.JarvixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceScreen(viewModel: JarvixViewModel, onBack: () -> Unit) {
    val coreState by viewModel.coreState.collectAsState()
    var spokenCommand by remember { mutableStateOf("Say or tap to speak a command") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("JARVIX VOICE ASSISTANT", color = Color(0xFF00FFFF), letterSpacing = 2.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF00FFFF))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF030712))
            )
        },
        containerColor = Color(0xFF030712)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "VOICE RECOGNITION ACTIVE",
                    color = Color(0xFF00FF66),
                    fontSize = 12.sp,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"Hey Jarvix, wake up, flashlight...\"",
                    color = Color(0xFF8892B0),
                    fontSize = 14.sp
                )
            }

            // Big Voice Orb
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color(0xFF00FFFF).copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        spokenCommand = "Jarvix status report online."
                        viewModel.sendPrompt("Status report online.")
                    },
                    modifier = Modifier
                        .size(140.dp)
                        .background(Color(0xFF00FFFF), shape = CircleShape)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Speak", tint = Color(0xFF030712), modifier = Modifier.size(64.dp))
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "COMMAND RECOGNIZED:", color = Color(0xFF00FFFF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = spokenCommand, color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}
