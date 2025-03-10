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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.data.Workout
import java.util.Date

@Composable
fun WorkoutScreen(
    onExerciseClick: (String) -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var workout by remember { mutableStateOf<Workout?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var exerciseToDelete by remember { mutableStateOf<String?>(null) }
    
    // Load workout for current date
    LaunchedEffect(Unit) {
        val today = Date()
        val loadedWorkout = dataStorage.loadWorkout(today)
        Log.d("WorkoutScreen", "Loaded workout: $loadedWorkout")
        if (loadedWorkout != null) {
            Log.d("WorkoutScreen", "Exercises: ${loadedWorkout.exercises}")
            loadedWorkout.exercises.forEach { exercise ->
                Log.d("WorkoutScreen", "Exercise: ${exercise.name}, Sets: ${exercise.sets}")
            }
        }
        workout = loadedWorkout
    }
    
    // Refresh workout when screen becomes visible again
    LaunchedEffect(Unit) {
        val today = Date()
        val loadedWorkout = dataStorage.loadWorkout(today)
        workout = loadedWorkout
    }
    
    if (workout == null) {
        Text(
            text = "No workout for today",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(workout!!.exercises) { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExerciseClick(exercise.name) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = exercise.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    exerciseToDelete = exercise.name
                                    showDeleteDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete exercise",
                                    tint = Color(0xFFF44336)
                                )
                            }
                        }
                        
                        if (exercise.sets.isNotEmpty()) {
                            Text(
                                text = "Sets:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                            
                            exercise.sets.forEach { set ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, bottom = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Set ${set.setNumber}",
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${set.weight}kg x ${set.reps} reps",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Exercise") },
            text = { Text("Are you sure you want to delete this exercise?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val updatedExercises = workout!!.exercises.filter { it.name != exerciseToDelete }
                        val updatedWorkout = Workout(workout!!.date, updatedExercises)
                        dataStorage.saveWorkout(updatedWorkout)
                        workout = updatedWorkout
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
} 