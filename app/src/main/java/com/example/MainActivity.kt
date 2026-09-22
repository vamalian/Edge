package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.JarvixViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: JarvixViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "startup") {
                    composable("startup") {
                        StartupScreen(viewModel = viewModel) {
                            navController.navigate("main") {
                                popUpTo("startup") { inclusive = true }
                            }
                        }
                    }
                    composable("main") {
                        MainCommandCenterScreen(viewModel = viewModel) { route ->
                            navController.navigate(route)
                        }
                    }
                    composable("chat") {
                        AiChatScreen(viewModel = viewModel) { navController.popBackStack() }
                    }
                    composable("voice") {
                        VoiceScreen(viewModel = viewModel) { navController.popBackStack() }
                    }
                    composable("controls") {
                        DeviceControlScreen(viewModel = viewModel) { navController.popBackStack() }
                    }
                    composable("owner_auth") {
                        OwnerAuthScreen(
                            viewModel = viewModel,
                            onVerified = {
                                navController.navigate("owner_dashboard") {
                                    popUpTo("owner_auth") { inclusive = true }
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("owner_dashboard") {
                        OwnerDashboardScreen(
                            viewModel = viewModel,
                            onLogout = {
                                navController.navigate("main") {
                                    popUpTo("owner_dashboard") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("status") {
                        SystemStatusScreen(viewModel = viewModel) { navController.popBackStack() }
                    }
                    composable("settings") {
                        SettingsScreen(viewModel = viewModel) { navController.popBackStack() }
                    }
                    composable("about") {
                        AboutScreen { navController.popBackStack() }
                    }
                }
            }
        }
    }
}
