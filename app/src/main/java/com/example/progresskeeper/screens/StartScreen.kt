package com.example.progresskeeper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.data.Workout
import java.util.*

@Composable
fun StartScreen(
    onStartWorkoutClick: () -> Unit,
    onCopyWorkoutClick: () -> Unit,
    onExerciseClick: (String) -> Unit,
    onAddExerciseClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onHelpClick: () -> Unit,
    onReportClick: () -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var selectedDate by remember { mutableStateOf(Date()) }
    var hasWorkout by remember { mutableStateOf(false) }
    var currentWorkout by remember { mutableStateOf<Workout?>(null) }

    LaunchedEffect(selectedDate) {
        val workout = dataStorage.loadWorkout(selectedDate)
        hasWorkout = workout != null
        currentWorkout = workout
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF424242))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Progress Keeper",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onCalendarClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Calendar",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                IconButton(
                    onClick = onAddExerciseClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Exercise",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                IconButton(
                    onClick = onHelpClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Help",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                IconButton(
                    onClick = onReportClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Weekly Report",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
        
        val calendar = Calendar.getInstance().apply { time = selectedDate }
        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val daySuffix = when (dayOfMonth) {
            1, 21, 31 -> "st"
            2, 22 -> "nd"
            3, 23 -> "rd"
            else -> "th"
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val allWorkouts = dataStorage.loadAllWorkouts()
                        val previousWorkout = allWorkouts
                            .filter { it.date.before(selectedDate) }
                            .maxByOrNull { it.date.time }
                        
                        if (previousWorkout != null) {
                            selectedDate = previousWorkout.date
                        }
                    },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous workout",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Text(
                    text = "$dayOfWeek, $month $dayOfMonth$daySuffix",
                    modifier = Modifier,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                IconButton(
                    onClick = {
                        val allWorkouts = dataStorage.loadAllWorkouts()
                        val nextWorkout = allWorkouts
                            .filter { it.date.after(selectedDate) }
                            .minByOrNull { it.date.time }
                        
                        if (nextWorkout != null) {
                            selectedDate = nextWorkout.date
                        } else {
                            selectedDate = Date()
                        }
                    },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next workout",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        
        if (hasWorkout) {
            WorkoutScreen(
                date = selectedDate,
                onExerciseClick = onExerciseClick,
                onStartWorkoutClick = onStartWorkoutClick,
                onCopyWorkoutClick = onCopyWorkoutClick,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "No workout for this date",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 32.dp)
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
    }
} 