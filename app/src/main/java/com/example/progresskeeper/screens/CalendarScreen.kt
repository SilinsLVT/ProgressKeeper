package com.example.progresskeeper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.data.Workout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    onDaySelected: (Date) -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    var workouts by remember { mutableStateOf<List<Workout>>(emptyList()) }
    
    // Load all workouts
    LaunchedEffect(Unit) {
        workouts = dataStorage.loadAllWorkouts()
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Month and year header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    .format(currentMonth.time),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Days of week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        
        // Calendar grid
        val calendar = currentMonth.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        
        val weeks = (firstDayOfWeek + daysInMonth - 1 + 6) / 7
        
        Column {
            repeat(weeks) { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(7) { dayOfWeek ->
                        val dayNumber = week * 7 + dayOfWeek - firstDayOfWeek + 2
                        if (dayNumber in 1..daysInMonth) {
                            val dayCalendar = currentMonth.clone() as Calendar
                            dayCalendar.set(Calendar.DAY_OF_MONTH, dayNumber)
                            val dayDate = dayCalendar.time
                            
                            val hasWorkout = workouts.any { workout ->
                                val workoutCalendar = Calendar.getInstance().apply {
                                    time = workout.date
                                }
                                workoutCalendar.get(Calendar.YEAR) == dayCalendar.get(Calendar.YEAR) &&
                                workoutCalendar.get(Calendar.MONTH) == dayCalendar.get(Calendar.MONTH) &&
                                workoutCalendar.get(Calendar.DAY_OF_MONTH) == dayCalendar.get(Calendar.DAY_OF_MONTH)
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .background(
                                        if (hasWorkout) Color(0xFF4CAF50).copy(alpha = 0.2f)
                                        else Color.Transparent
                                    )
                                    .clickable { onDaySelected(dayDate) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    color = if (hasWorkout) Color(0xFF4CAF50) else Color.Black
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutPreviewDialog(
    workout: Workout,
    onCopy: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                    .format(workout.date),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(workout.exercises) { exercise ->
                    Text(
                        text = exercise.name,
                        fontSize = 16.sp
                    )
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Copy",
                    color = Color(0xFF4CAF50),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable(onClick = onCopy)
                )
                Text(
                    text = "Close",
                    color = Color(0xFFF44336),
                    modifier = Modifier.clickable(onClick = onDismiss)
                )
            }
        }
    }
} 