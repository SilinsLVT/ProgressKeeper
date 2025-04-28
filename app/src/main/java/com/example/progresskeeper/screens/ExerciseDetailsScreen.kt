package com.example.progresskeeper.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.data.ExerciseSet
import com.example.progresskeeper.data.Workout
import com.example.progresskeeper.data.WorkoutExercise
import java.util.Date

@Composable
fun ExerciseDetailsScreen(
    exercise: String,
    onSaveClick: () -> Unit,
    onHomeClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var weight by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    val sets = remember { mutableStateListOf<ExerciseSet>() }
    var isEditMode by remember { mutableStateOf(false) }
    var editingSetIndex by remember { mutableStateOf(-1) }

    DisposableEffect(exercise) {
        val savedSets = dataStorage.loadExerciseSets(exercise)
        sets.clear()
        sets.addAll(savedSets)
        
        onDispose { }
    }
    
    fun saveExerciseData() {
        dataStorage.saveExerciseSets(exercise, sets)
        val today = Date()
        val currentWorkout = dataStorage.loadWorkout(today) ?: Workout(today, emptyList())
        
        val updatedExercises = currentWorkout.exercises.map { workoutExercise ->
            if (workoutExercise.name == exercise) {
                WorkoutExercise(exercise, sets)
            } else {
                workoutExercise
            }
        }
        
        val updatedWorkout = Workout(today, updatedExercises)
        dataStorage.saveWorkout(updatedWorkout)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = exercise,
            onCalendarClick = {},
            onAddClick = {},
            onHelpClick = onHelpClick,
            onHomeClick = onHomeClick
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = weight,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[.,]?\\d*$"))) {
                        weight = newValue
                    }
                },
                label = { Text("Weight") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = reps,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[.,]?\\d*$"))) {
                        reps = newValue
                    }
                },
                label = { Text("Reps") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (weight.isNotEmpty() && reps.isNotEmpty()) {
                        if (isEditMode && editingSetIndex >= 0 && editingSetIndex < sets.size) {
                            val currentSet = sets[editingSetIndex]
                            sets[editingSetIndex] = ExerciseSet(currentSet.setNumber, weight, reps)
                            isEditMode = false
                            editingSetIndex = -1
                        } else {
                            sets.add(ExerciseSet(sets.size + 1, weight, reps))
                        }
                        weight = ""
                        reps = ""
                        saveExerciseData()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEditMode) Color(0xFFFF9800) else Color(0xFF4CAF50)
                )
            ) {
                Text(if (isEditMode) "Update" else "Save")
            }

            if (isEditMode) {
                Button(
                    onClick = {
                        isEditMode = false
                        editingSetIndex = -1
                        weight = ""
                        reps = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9E9E9E)
                    )
                ) {
                    Text("Cancel")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sets) { set ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .drawBehind {
                                drawLine(
                                    color = Color.Gray.copy(alpha = 0.5f),
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 2f
                                )
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Set ${set.setNumber}")
                            Text("${set.weight}kg")
                            Text("${set.reps} reps")
                            
                            Row {
                                IconButton(
                                    onClick = {
                                        isEditMode = true
                                        editingSetIndex = sets.indexOf(set)
                                        weight = set.weight
                                        reps = set.reps
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit set",
                                        tint = Color(0xFF2196F3)
                                    )
                                }
                                
                                IconButton(
                                    onClick = {
                                        sets.remove(set)
                                        
                                        // Renumber sets
                                        for (i in 0 until sets.size) {
                                            sets[i] = ExerciseSet(i + 1, sets[i].weight, sets[i].reps)
                                        }
                                        
                                        saveExerciseData()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete set",
                                        tint = Color(0xFFF44336)
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