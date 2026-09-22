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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.JarvixViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerAuthScreen(
    viewModel: JarvixViewModel,
    onVerified: () -> Unit,
    onBack: () -> Unit
) {
    var masterKeyInput by remember { mutableStateOf("") }
    var verificationStatus by remember { mutableStateOf("ENTER MASTER KEY") }
    var isError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MASTER KEY VERIFICATION", color = Color(0xFF00FFFF), letterSpacing = 2.sp) },
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
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = "Secure Lock",
                tint = Color(0xFF00FFFF),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = verificationStatus,
                color = if (isError) Color(0xFFFF3366) else Color(0xFF00FFFF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = masterKeyInput,
                onValueChange = { masterKeyInput = it },
                placeholder = { Text("Enter Master Key (4000)", color = Color(0xFF8892B0)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00FFFF),
                    unfocusedBorderColor = Color(0xFF112240),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    verificationStatus = "VERIFYING..."
                    val success = viewModel.verifyMasterKey(masterKeyInput)
                    if (success) {
                        verificationStatus = "ACCESS GRANTED ✓"
                        isError = false
                        onVerified()
                    } else {
                        verificationStatus = "ACCESS DENIED"
                        isError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFFF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "UNLOCK OWNER DASHBOARD",
                    color = Color(0xFF030712),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "PRIVATE & SECURED ENCRYPTED CHANNEL",
                color = Color(0xFF8892B0),
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        }
    }
}
