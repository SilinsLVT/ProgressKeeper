package com.example.progresskeeper.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable

@Composable
fun HelpExercisesScreen(
    category: String,
    onExerciseClick: (String) -> Unit,
    onHomeClick: () -> Unit
) {
    val exercises = when (category) {
        "Traps" -> listOf(
            "Barbell Shrugs",
            "Dumbbell Shrugs",
            "Behind-the-Back Smith Machine Shrugs",
            "Rack Pulls",
            "Face Pulls"
        )
        else -> listOf("Coming soon...")
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = category,
            onCalendarClick = {},
            onAddClick = {},
            onHelpClick = {},
            onHomeClick = onHomeClick
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(exercises) { exercise ->
                Text(
                    text = exercise,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExerciseClick(exercise) }
                        .padding(vertical = 12.dp),
                    fontSize = 18.sp
                )
            }
        }
    }
} 