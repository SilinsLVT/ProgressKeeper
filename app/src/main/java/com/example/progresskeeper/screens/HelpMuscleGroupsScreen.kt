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
fun HelpMuscleGroupsScreen(
    onMuscleGroupSelected: (String) -> Unit,
    onHomeClick: () -> Unit
) {
    val muscleGroups = listOf(
        "Traps",
        "Shoulders",
        "Chest",
        "Back",
        "Triceps",
        "Biceps",
        "Forearms",
        "Legs",
        "Calves"
    )
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = "Help - Muscle Groups",
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
            items(muscleGroups) { muscleGroup ->
                Text(
                    text = muscleGroup,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onMuscleGroupSelected(muscleGroup) }
                        .padding(vertical = 12.dp),
                    fontSize = 18.sp
                )
            }
        }
    }
} 