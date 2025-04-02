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
    date: Date,
    onExerciseClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var workout by remember { mutableStateOf<Workout?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var exerciseToDelete by remember { mutableStateOf<String?>(null) }

    // Load workout when date changes
    LaunchedEffect(date) {
        workout = dataStorage.loadWorkout(date)
    }

    // Reload workout after deletion
    LaunchedEffect(showDeleteDialog) {
        if (!showDeleteDialog) {
            workout = dataStorage.loadWorkout(date)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            workout == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No workout for this date",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
            workout?.exercises?.isEmpty() == true -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No exercises added yet",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
            else -> {
                workout?.let { currentWorkout ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(currentWorkout.exercises) { exercise ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = exercise.name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black,
                                            modifier = Modifier.clickable { onExerciseClick(exercise.name) }
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
                                        Column(
                                            modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            exercise.sets.forEach { set ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "Set ${set.setNumber}",
                                                        color = Color.Black,
                                                        fontSize = 16.sp
                                                    )
                                                    Text(
                                                        text = "${set.weight}kg × ${set.reps} reps",
                                                        color = Color.Black,
                                                        fontSize = 16.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && exerciseToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                exerciseToDelete = null
            },
            title = { Text("Delete Exercise") },
            text = { Text("Are you sure you want to delete this exercise?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val updatedExercises = workout?.exercises?.filter { it.name != exerciseToDelete } ?: emptyList()
                        val updatedWorkout = Workout(date, updatedExercises)
                        dataStorage.saveWorkout(updatedWorkout)
                        showDeleteDialog = false
                        exerciseToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        exerciseToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
} 