package com.example.progresskeeper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.data.Workout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun CalendarScreen(
    onDaySelected: (Date) -> Unit,
    onHomeClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var workouts by remember { mutableStateOf<List<Workout>>(emptyList()) }
    val listState = rememberLazyListState()

    fun refreshWorkouts() {
        workouts = dataStorage.loadAllWorkouts()
    }

    LaunchedEffect(Unit) {
        refreshWorkouts()
    }

    val months = remember {
        val calendar = Calendar.getInstance()
        val startYear = 2020
        val endYear = 2050
        val monthsList = mutableListOf<Calendar>()

        calendar.set(Calendar.YEAR, startYear)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        
        while (calendar.get(Calendar.YEAR) <= endYear) {
            monthsList.add(calendar.clone() as Calendar)
            calendar.add(Calendar.MONTH, 1)
        }
        
        monthsList
    }

    LaunchedEffect(Unit) {
        val currentMonth = Calendar.getInstance()
        val currentMonthIndex = months.indexOfFirst { month ->
            month.get(Calendar.YEAR) == currentMonth.get(Calendar.YEAR) &&
            month.get(Calendar.MONTH) == currentMonth.get(Calendar.MONTH)
        }
        if (currentMonthIndex != -1) {
            listState.scrollToItem(currentMonthIndex)
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = "Calendar",
            onCalendarClick = {},
            onAddClick = {},
            onHelpClick = { onHelpClick() },
            onHomeClick = onHomeClick,
            workoutCount = workouts.size
        )
        
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(months) { month ->
                MonthView(
                    month = month,
                    workouts = workouts,
                    onDaySelected = onDaySelected
                )
            }
        }
    }
}

@Composable
fun AppHeader(
    title: String,
    onCalendarClick: () -> Unit,
    onAddClick: () -> Unit,
    onHelpClick: () -> Unit,
    onHomeClick: () -> Unit,
    workoutCount: Int? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF424242))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onHomeClick,
                modifier = Modifier.size(48.dp)
            ) {
                Text(
                    text = "PK",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            
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
        }
        
        if (workoutCount != null) {
            Text(
                text = "$workoutCount workouts in total",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MonthView(
    month: Calendar,
    workouts: List<Workout>,
    onDaySelected: (Date) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                .format(month.time),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        val calendar = month.clone() as Calendar
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
                            val dayCalendar = month.clone() as Calendar
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
                                        else Color.Transparent,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                                    .clickable { onDaySelected(dayDate) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayNumber.toString(),
                                    color = if (hasWorkout) Color(0xFF4CAF50) else Color.Black,
                                    textAlign = TextAlign.Center
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
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var workouts by remember { mutableStateOf<List<Workout>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        workouts = dataStorage.loadAllWorkouts()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .drawBehind {
                                drawLine(
                                    color = Color.LightGray,
                                    start = Offset(0f, size.height + 8.dp.toPx()),
                                    end = Offset(size.width, size.height + 8.dp.toPx()),
                                    strokeWidth = 1f
                                )
                            }
                    ) {
                        Text(
                            text = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
                                .format(workout.date),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(workout.exercises) { exercise ->
                            Column {
                                Text(
                                    text = exercise.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                if (exercise.sets.isNotEmpty()) {
                                    Column(
                                        modifier = Modifier.padding(start = 16.dp)
                                    ) {
                                        exercise.sets.forEach { set ->
                                            Text(
                                                text = "Set ${set.setNumber}: ${set.weight}kg x ${set.reps} reps",
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
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
                            text = "Delete",
                            color = Color(0xFFF44336),
                            modifier = Modifier.clickable {
                                showDeleteDialog = true
                            }
                        )
                    }
                }
                
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFFF44336)
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = { Text("Delete Workout?") },
            text = { Text("Are you sure you want to delete this workout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        dataStorage.deleteWorkout(workout.date)
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
} 