package com.example.progresskeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.data.Workout
import com.example.progresskeeper.navigation.Screen
import com.example.progresskeeper.screens.ExercisesScreen
import com.example.progresskeeper.screens.WorkoutCategoriesScreen
import com.example.progresskeeper.screens.ExerciseDetailsScreen
import com.example.progresskeeper.screens.WorkoutScreen
import com.example.progresskeeper.screens.CalendarScreen
import com.example.progresskeeper.screens.WorkoutPreviewDialog
import com.example.progresskeeper.ui.theme.ProgressKeeperTheme
import java.util.Date
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgressKeeperTheme {
                val navController = rememberNavController()
                var selectedWorkout by remember { mutableStateOf<Workout?>(null) }
                val context = LocalContext.current
                val dataStorage = remember { DataStorage(context) }
                
                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Start.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Start.route) {
                            StartScreen(
                                onStartWorkoutClick = {
                                    navController.navigate(Screen.WorkoutCategories.route)
                                },
                                onCopyWorkoutClick = {
                                    navController.navigate(Screen.Calendar.route)
                                },
                                onExerciseClick = { exercise ->
                                    navController.navigate(Screen.ExerciseDetails.createRoute(exercise))
                                },
                                onAddExerciseClick = {
                                    navController.navigate(Screen.WorkoutCategories.route)
                                },
                                onCalendarClick = {
                                    navController.navigate(Screen.Calendar.route)
                                }
                            )
                        }
                        
                        composable(
                            route = Screen.Workout.route,
                            arguments = listOf(
                                navArgument("date") { type = NavType.LongType }
                            )
                        ) { backStackEntry ->
                            val dateLong = backStackEntry.arguments?.getLong("date") ?: return@composable
                            val date = Date(dateLong)
                            WorkoutScreen(
                                date = date,
                                onExerciseClick = { exercise ->
                                    navController.navigate(Screen.ExerciseDetails.createRoute(exercise))
                                },
                                onStartWorkoutClick = {
                                    navController.navigate(Screen.WorkoutCategories.route)
                                },
                                onCopyWorkoutClick = {
                                    navController.navigate(Screen.Calendar.route)
                                }
                            )
                        }
                        
                        composable(Screen.WorkoutCategories.route) {
                            WorkoutCategoriesScreen(
                                onCategorySelected = { category ->
                                    navController.navigate(Screen.Exercises.createRoute(category))
                                },
                                onHomeClick = {
                                    navController.navigate(Screen.Start.route)
                                }
                            )
                        }
                        
                        composable(
                            route = Screen.Exercises.route,
                            arguments = listOf(
                                navArgument("category") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val category = backStackEntry.arguments?.getString("category") ?: return@composable
                            ExercisesScreen(
                                category = category,
                                onExerciseClick = { exercise ->
                                    navController.navigate(Screen.ExerciseDetails.createRoute(exercise))
                                },
                                onHomeClick = {
                                    navController.navigate(Screen.Start.route)
                                }
                            )
                        }

                        composable(
                            route = Screen.ExerciseDetails.route,
                            arguments = listOf(
                                navArgument("exercise") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val exercise = backStackEntry.arguments?.getString("exercise") ?: return@composable
                            ExerciseDetailsScreen(
                                exercise = exercise,
                                onSaveClick = {
                                    navController.navigate(Screen.Workout.route)
                                },
                                onHomeClick = {
                                    navController.navigate(Screen.Start.route)
                                }
                            )
                        }
                        
                        composable(Screen.Calendar.route) {
                            CalendarScreen(
                                onDaySelected = { date ->
                                    val workout = dataStorage.loadWorkout(date)
                                    if (workout != null) {
                                        selectedWorkout = workout
                                    }
                                },
                                onHomeClick = {
                                    navController.navigate(Screen.Start.route)
                                }
                            )
                        }
                    }
                }
                if (selectedWorkout != null) {
                    WorkoutPreviewDialog(
                        workout = selectedWorkout!!,
                        onCopy = {
                            val today = Date()
                            val copiedWorkout = Workout(today, selectedWorkout!!.exercises)
                            dataStorage.saveWorkout(copiedWorkout)
                            selectedWorkout = null
                            navController.navigate(Screen.Start.route)
                        },
                        onDismiss = {
                            selectedWorkout = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    onCalendarClick: () -> Unit = {},
    onAddClick: () -> Unit,
    onHelpClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
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
                onClick = onAddClick,
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
        }
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onStartWorkoutClick: () -> Unit,
    onCopyWorkoutClick: () -> Unit,
    onExerciseClick: (String) -> Unit,
    onAddExerciseClick: () -> Unit,
    onCalendarClick: () -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    var selectedDate by remember { mutableStateOf(Date()) }
    var hasWorkout by remember { mutableStateOf(false) }
    var currentWorkout by remember { mutableStateOf<Workout?>(null) }
    
    // Load workout for selected date
    LaunchedEffect(selectedDate) {
        val workout = dataStorage.loadWorkout(selectedDate)
        hasWorkout = workout != null
        currentWorkout = workout
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AppHeader(
            onAddClick = onAddExerciseClick,
            onCalendarClick = onCalendarClick
        )
        
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
                        calendar.add(Calendar.DAY_OF_MONTH, -1)
                        selectedDate = calendar.time
                    },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous day",
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
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                        selectedDate = calendar.time
                    },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next day",
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
            WorkoutScreen(
                date = selectedDate,
                onExerciseClick = onExerciseClick,
                onStartWorkoutClick = onStartWorkoutClick,
                onCopyWorkoutClick = onCopyWorkoutClick,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}