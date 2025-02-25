package com.example.progresskeeper.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExercisesScreen(
    category: String,
    onExerciseClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val exercises = when (category) {
        "Traps" -> listOf(
            "Barbell Shrugs",
            "Dumbbell Shrugs",
            "Behind-the-Back Smith Machine Shrugs",
            "Rack Pulls",
            "Face Pulls"
        )
        "Shoulders" -> listOf(
            "Military Press",
            "Lateral Raises",
            "Front Raises",
            "Reverse Flyes",
            "Arnold Press"
        )
        "Chest" -> listOf(
            "Bench Press",
            "Incline Dumbbell Press",
            "Dips",
            "Cable Flyes",
            "Push-Ups"
        )
        "Back" -> listOf(
            "Pull-Ups",
            "Barbell Rows",
            "Lat Pulldowns",
            "Deadlifts",
            "T-Bar Rows"
        )
        "Triceps" -> listOf(
            "Tricep Pushdowns",
            "Skull Crushers",
            "Close-Grip Bench Press",
            "Overhead Tricep Extensions",
            "Diamond Push-Ups"
        )
        "Biceps" -> listOf(
            "Barbell Curls",
            "Hammer Curls",
            "Preacher Curls",
            "Incline Dumbbell Curls",
            "Concentration Curls"
        )
        "Forearms" -> listOf(
            "Wrist Curls",
            "Reverse Wrist Curls",
            "Farmers Walks",
            "Plate Pinches",
            "Behind-the-Back Wrist Curls"
        )
        "Legs" -> listOf(
            "Squats",
            "Romanian Deadlifts",
            "Leg Press",
            "Lunges",
            "Leg Extensions"
        )
        "Calves" -> listOf(
            "Standing Calf Raises",
            "Seated Calf Raises",
            "Donkey Calf Raises",
            "Jump Rope",
            "Single-Leg Calf Raises"
        )
        else -> emptyList()
    }

    val filteredExercises = exercises.filter { exercise ->
        exercise.lowercase().contains(searchQuery.lowercase())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search exercises...") },
                singleLine = true
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(filteredExercises) { exercise ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = Color.LightGray.copy(alpha = 0.5f),
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1f
                            )
                        }
                        .clickable { onExerciseClick(exercise) }
                        .padding(vertical = 16.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = exercise,
                        color = Color.Black
                    )
                }
            }
        }
    }
}