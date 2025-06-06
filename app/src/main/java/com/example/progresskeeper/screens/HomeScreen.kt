package com.example.progresskeeper.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.progresskeeper.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ProgressKeeper") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.WeeklyReport.route) }) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = "Weekly Report"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Information"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
    }
} 