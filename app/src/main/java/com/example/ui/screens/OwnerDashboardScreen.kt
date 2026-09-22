package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.viewmodel.JarvixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(viewModel: JarvixViewModel, onLogout: () -> Unit) {
    val logs by viewModel.systemLogs.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OWNER COMMAND DASHBOARD", color = Color(0xFF00FFFF), letterSpacing = 2.sp) },
                actions = {
                    IconButton(onClick = {
                        viewModel.logoutOwner()
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color(0xFF00FFFF))
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
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "SYSTEM STATUS", color = Color(0xFF00FFFF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "JARVIX CORE: ONLINE & SECURED", color = Color(0xFF00FF66), fontSize = 14.sp)
                        Text(text = "AI MODEL: GEMINI 3.5 FLASH", color = Color(0xFF8892B0), fontSize = 12.sp)
                        Text(text = "AUTHENTICATION: OWNER VERIFIED ✓", color = Color(0xFF8892B0), fontSize = 12.sp)
                    }
                }
            }

            item {
                Text(
                    text = "SECURE SYSTEM AUDIT LOGS",
                    color = Color(0xFF00FFFF),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(logs) { log ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F).copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = log.category, color = Color(0xFF00FFFF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(log.timestamp)), color = Color(0xFF8892B0), fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = log.message, color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
