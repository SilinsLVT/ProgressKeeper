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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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


// Virs šī ir importētas nepieciešamās bibliotēkas un komponentes:
// UI elementi, datu klases un komponenti

// Galvenā funkcija, kas veido treniņu ar tā parametriem
@Composable
fun WorkoutScreen(
    date: Date,
    onExerciseClick: (String) -> Unit,
    onStartWorkoutClick: () -> Unit,
    onCopyWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Inicializē "context" mainīgo, kā arī datu glabāšanas objektu
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }

    // Saglabā pašreizējo treniņu
    var workout by remember { mutableStateOf<Workout?>(null) }

    // Parāda / neparāda dzēšanas apstiprināšanas logu
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Saglabā vingrinājuma nosaukumu, kuru vēlas dzēst
    var exerciseToDelete by remember { mutableStateOf<String?>(null) }

    // Ielādē treniņu, ja mainās datums
    LaunchedEffect(date) {
        workout = dataStorage.loadWorkout(date)
    }

    // Atjauno treniņa skatu pēc dzēšanas
    LaunchedEffect(showDeleteDialog) {
        if (!showDeleteDialog) {
            workout = dataStorage.loadWorkout(date)
        }
    }

    // Galvenais ekrāna skats
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {

            // Ja treniņa nav, parāda sākuma ekrānu, kur rāda pogas u.t.t.
            workout == null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    // Parāda ziņojumu vingrinājumu vietā, jatreniņa nav
                    Text(
                        text = "No workout for this date",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    
                    // Galvenā skata pogas "Copy previous workout" un "Start new workout"
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        // "Start new workout" poga
                        Button(
                            onClick = onStartWorkoutClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "Start new workout",
                                color = Color.Black
                            )
                        }

                        //"Copy previous workout" poga
                        Button(
                            onClick = onCopyWorkoutClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "Copy previous workout",
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Ja treniņam nav pievienots neviens vingrinājums
            workout?.exercises?.isEmpty() == true -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "No exercises added yet",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    
                    // Pogas kas parādās, ja nav neviens vingrinājums pievienots treniņam
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Poga vingrinājumu pievienošanai
                        Button(
                            onClick = onStartWorkoutClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "Add exercises",
                                color = Color.Black
                            )
                        }
                        
                        // Poga iepriekšēja treniņa kopēšanai
                        Button(
                            onClick = onCopyWorkoutClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "Copy previous workout",
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Ja treniņā ir vingrinājumi, parāda tos un to datus
            else -> {
                workout?.let { currentWorkout ->

                    // Parāda visus vingrinājumus
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(currentWorkout.exercises) { exercise ->

                            // "Card" iekš kura ir katra vingrinājuma dati
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .clickable { onExerciseClick(exercise.name) },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {

                                    // Parāda vingrinājuma nosaukumu un dzēšanas pogu
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = exercise.name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )

                                        // Dzēšanas poga
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
                                    
                                    // Parāda vingrinājuma datus (piegājienus, svaru un atkārtojuma reizes)
                                    if (exercise.sets.isNotEmpty()) {
                                        Column(
                                            modifier = Modifier.padding(start = 8.dp, top = 16.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            exercise.sets.forEach { set ->

                                                // Parāda piegājiena datus
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

    // Dzēšanas logs, kas parādās ja lietotājs nospiež dzēšanas (gružkastes) pogu
    if (showDeleteDialog && exerciseToDelete != null) {
        AlertDialog(

            // Aizver logu, ja lietotājs nospiež uz "cancel" pogas, vai ārpus loga
            onDismissRequest = {
                showDeleteDialog = false
                exerciseToDelete = null
            },
            title = { Text("Delete Exercise") },
            text = { Text("Are you sure you want to delete this exercise?") },
            // "Delete" poga
            confirmButton = {
                TextButton(
                    onClick = {

                        // Paņem vingrinājumu kuram nospieda dzēšanas pogu, un to izdzēš
                        val updatedExercises = workout?.exercises?.filter { it.name != exerciseToDelete } ?: emptyList()
                        val updatedWorkout = Workout(date, updatedExercises)

                        // Saglabā izmaiņas pēc vingrinājuma dzēšanas
                        dataStorage.saveWorkout(updatedWorkout)
                        showDeleteDialog = false
                        exerciseToDelete = null
                    }
                ) {
                    Text("Delete", color = Color(0xFF8B0000))
                }
            },
            // "Cancel" poga
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        exerciseToDelete = null
                    }
                ) {
                    Text("Cancel", color = Color(0xFF4CAF50))
                }
            }
        )
    }
} 