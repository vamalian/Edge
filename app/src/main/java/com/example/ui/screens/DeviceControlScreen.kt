package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.JarvixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceControlScreen(viewModel: JarvixViewModel, onBack: () -> Unit) {
    val battery by viewModel.batteryInfo.collectAsState()
    val network by viewModel.networkInfo.collectAsState()
    var flashState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ANDROID DEVICE CONTROLS", color = Color(0xFF00FFFF), letterSpacing = 2.sp) },
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
            item {
                ControlCard(
                    title = "FLASHLIGHT TORCH",
                    subtitle = if (flashState) "Active (ON)" else "Inactive (OFF)",
                    icon = Icons.Default.FlashlightOn,
                    buttonText = if (flashState) "TURN OFF" else "TURN ON"
                ) {
                    flashState = viewModel.deviceManager.toggleFlashlight()
                    viewModel.logSystem("Hardware", "Flashlight toggled: $flashState")
                }
            }

            item {
                ControlCard(
                    title = "SYSTEM SETTINGS",
                    subtitle = "Open Android System Preferences",
                    icon = Icons.Default.Settings,
                    buttonText = "OPEN SETTINGS"
                ) {
                    viewModel.deviceManager.openSettings()
                    viewModel.logSystem("Hardware", "Launched Android settings.")
                }
            }

            item {
                ControlCard(
                    title = "BATTERY STATUS",
                    subtitle = battery,
                    icon = Icons.Default.BatteryChargingFull,
                    buttonText = "REFRESH"
                ) {
                    viewModel.logSystem("Hardware", "Battery status checked: $battery")
                }
            }

            item {
                ControlCard(
                    title = "NETWORK STATUS",
                    subtitle = network,
                    icon = Icons.Default.Wifi,
                    buttonText = "CHECK"
                ) {
                    viewModel.logSystem("Hardware", "Network status checked: $network")
                }
            }
        }
    }
}

@Composable
fun ControlCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A192F)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(icon, contentDescription = null, tint = Color(0xFF00FFFF), modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = subtitle, color = Color(0xFF8892B0), fontSize = 12.sp)
                }
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFFF))
            ) {
                Text(text = buttonText, color = Color(0xFF030712), fontSize = 11.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
    }
}
