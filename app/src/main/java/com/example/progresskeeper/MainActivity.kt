package com.example.progresskeeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.progresskeeper.data.DataStorage
import com.example.progresskeeper.navigation.Screen
import com.example.progresskeeper.screens.ExercisesScreen
import com.example.progresskeeper.screens.WorkoutCategoriesScreen
import com.example.progresskeeper.screens.ExerciseDetailsScreen
import com.example.progresskeeper.screens.WorkoutScreen
import com.example.progresskeeper.ui.theme.ProgressKeeperTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgressKeeperTheme {
                val navController = rememberNavController()
                
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
                                    // TODO: Implement copy workout functionality
                                },
                                onExerciseClick = { exercise ->
                                    navController.navigate(Screen.ExerciseDetails.createRoute(exercise))
                                }
                            )
                        }
                        
                        composable(Screen.Workout.route) {
                            WorkoutScreen { exercise ->
                                navController.navigate(Screen.ExerciseDetails.createRoute(exercise))
                            }
                        }
                        
                        composable(Screen.WorkoutCategories.route) {
                            WorkoutCategoriesScreen { category ->
                                navController.navigate(Screen.Exercises.createRoute(category))
                            }
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
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onStartWorkoutClick: () -> Unit,
    onCopyWorkoutClick: () -> Unit,
    onExerciseClick: (String) -> Unit
) {
    val context = LocalContext.current
    val dataStorage = remember { DataStorage(context) }
    val hasWorkoutToday = remember { mutableStateOf(false) }
    
    // Load workout for current date
    LaunchedEffect(Unit) {
        val today = Date()
        val workout = dataStorage.loadWorkout(today)
        hasWorkoutToday.value = workout != null
    }
    
    if (hasWorkoutToday.value) {
        WorkoutScreen(onExerciseClick = onExerciseClick)
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onCopyWorkoutClick,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(text = "Copy Workout")
            }
            
            Button(
                onClick = onStartWorkoutClick,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(text = "Start Workout")
            }
        }
    }
}