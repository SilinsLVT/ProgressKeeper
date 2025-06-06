package com.example.progresskeeper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.navigation.NavController
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.navigation.Screen
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*
import java.util.Calendar

@Composable
fun WeeklyReportScreen(navController: NavController) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    val currentWeek = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

    val weekStart = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
    val weeklyWorkouts = dataStorage.loadAllWorkouts().filter { workout ->
        workout.date >= weekStart
    }

    val workoutCount = weeklyWorkouts.size
    val totalWeight = weeklyWorkouts.sumOf { workout ->
        workout.exercises.sumOf { exercise ->
            exercise.sets.sumOf { set ->
                try {
                    (set.weight.toDoubleOrNull() ?: 0.0) * (set.reps.toDoubleOrNull() ?: 0.0)
                } catch (e: NumberFormatException) {
                    0.0
                }
            }
        }
    }.toInt()
    val totalSets = weeklyWorkouts.sumOf { workout ->
        workout.exercises.sumOf { exercise ->
            exercise.sets.size.toDouble()
        }
    }.toInt()
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
            IconButton(
                onClick = { navController.navigate(Screen.Start.route) },
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
                text = "This weeks stats",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { navController.navigate(Screen.HelpMuscleGroups.route) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatisticBox(
                value = workoutCount.toString(),
                label = "Workouts completed this week",
                modifier = Modifier.fillMaxWidth()
            )
            
            StatisticBox(
                value = "$totalWeight kg",
                label = "Total weight lifted this week",
                modifier = Modifier.fillMaxWidth()
            )
            
            StatisticBox(
                value = totalSets.toString(),
                label = "Total sets performed this week",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun StatisticBox(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(
                width = 2.dp,
                color = Color(0xFF424242),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = label,
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
} 