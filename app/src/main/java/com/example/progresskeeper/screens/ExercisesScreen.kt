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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.data.Workout
import com.example.progresskeeper.data.WorkoutExercise
import java.util.Date

@Composable
fun ExercisesScreen(
    category: String,
    onExerciseClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val exercises = remember { mutableStateListOf<String>() }
    
    // Load saved exercises when the screen is first shown
    DisposableEffect(category) {
        val defaultExercises = when (category) {
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
        
        val savedExercises = dataStorage.loadExerciseNames(category)
        exercises.clear()
        exercises.addAll(savedExercises.ifEmpty { defaultExercises })
        
        onDispose { }
    }

    val filteredExercises = exercises.filter { exercise ->
        exercise.lowercase().contains(searchQuery.lowercase())
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = category,
            onCalendarClick = {},
            onAddClick = {},
            onHelpClick = onHelpClick,
            onHomeClick = onHomeClick
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
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
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search exercises...") },
                singleLine = true
            )
            IconButton(
                onClick = { showAddExerciseDialog = true },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add exercise",
                    tint = Color.Black
                )
            }
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
                        .clickable {
                            val today = Date()
                            val currentWorkout = dataStorage.loadWorkout(today) ?: Workout(today, emptyList())

                            if (!currentWorkout.exercises.any { it.name == exercise }) {
                                val updatedExercises = currentWorkout.exercises + WorkoutExercise(exercise, emptyList())
                                val updatedWorkout = Workout(today, updatedExercises)
                                dataStorage.saveWorkout(updatedWorkout)
                            }
                            
                            onExerciseClick(exercise)
                        }
                        .padding(vertical = 16.dp, horizontal = 16.dp),
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

    if (showAddExerciseDialog) {
        AddExerciseScreen(
            onDismiss = { showAddExerciseDialog = false },
            onSave = { exerciseName ->
                if (exerciseName.isBlank()) {
                    errorMessage = "Exercise name cannot be empty"
                    showErrorDialog = true
                    return@AddExerciseScreen
                }
                
                if (exercises.any { it.equals(exerciseName, ignoreCase = true) }) {
                    errorMessage = "This exercise already exists"
                    showErrorDialog = true
                    return@AddExerciseScreen
                }
                
                exercises.add(exerciseName)
                dataStorage.saveExerciseNames(category, exercises)
                showAddExerciseDialog = false
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}