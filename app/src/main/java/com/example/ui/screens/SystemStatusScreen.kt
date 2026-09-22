package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.JarvixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemStatusScreen(viewModel: JarvixViewModel, onBack: () -> Unit) {
    val battery by viewModel.batteryInfo.collectAsState()
    val network by viewModel.networkInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HOLOGRAPHIC SYSTEM STATUS", color = Color(0xFF00FFFF), letterSpacing = 2.sp) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { StatusItemCard("JARVIX AI CORE", "ONLINE & ACTIVE", Color(0xFF00FF66)) }
            item { StatusItemCard("VOICE RECOGNITION & TTS", "READY", Color(0xFF00FFFF)) }
            item { StatusItemCard("NEURAL LINK NETWORK", network, Color(0xFF00FFFF)) }
            item { StatusItemCard("BATTERY TELEMETRY", battery, Color(0xFF00A8FF)) }
            item { StatusItemCard("SECURITY PROTOCOL", "ENCRYPTED & SECURED", Color(0xFF00FF66)) }
        }
    }
}

@Composable
fun StatusItemCard(title: String, value: String, valueColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
