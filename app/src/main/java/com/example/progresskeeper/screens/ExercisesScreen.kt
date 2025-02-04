package com.example.progresskeeper.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExercisesScreen(category: String) {
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(exercises) { exercise ->
            Text(
                text = exercise,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
} 