package com.example.progresskeeper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.progresskeeper.screens.*
import java.util.Date

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Start.route) {
        composable(Screen.Start.route) {
            StartScreen(
                onStartWorkoutClick = {
                    navController.navigate(Screen.WorkoutCategories.route)
                },
                onCopyWorkoutClick = {
                    navController.navigate(Screen.Calendar.route)
                },
                onExerciseClick = { exercise: String ->
                    navController.navigate(Screen.ExerciseDetails.createRoute(exercise))
                },
                onAddExerciseClick = {
                    navController.navigate(Screen.WorkoutCategories.route)
                },
                onCalendarClick = {
                    navController.navigate(Screen.Calendar.route)
                },
                onHelpClick = {
                    navController.navigate(Screen.HelpMuscleGroups.route)
                },
                onReportClick = {
                    navController.navigate(Screen.WeeklyReport.route)
                }
            )
        }
        
        composable(Screen.WeeklyReport.route) {
            WeeklyReportScreen(navController = navController)
        }
        composable(
            route = Screen.ExerciseInstructions.route,
            arguments = listOf(
                navArgument("exercise") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val exercise = backStackEntry.arguments?.getString("exercise") ?: ""
            ExerciseInstructionsScreen(
                exercise = exercise,
                onHomeClick = { navController.navigate(Screen.Start.route) }
            )
        }
        
        composable(Screen.WorkoutCategories.route) {
            WorkoutCategoriesScreen(
                onCategorySelected = { category ->
                    navController.navigate(Screen.Exercises.createRoute(category))
                },
                onHomeClick = {
                    navController.navigate(Screen.Start.route)
                },
                onHelpClick = {
                    navController.navigate(Screen.HelpMuscleGroups.route)
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
                },
                onHelpClick = { category ->
                    navController.navigate(Screen.HelpExercises.createRoute(category))
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
                onHomeClick = {
                    navController.navigate(Screen.Start.route)
                },
                onHelpClick = {
                    navController.navigate(Screen.HelpExercises.createRoute(exercise))
                },
                onSaveClick = {
                    navController.navigate(Screen.Start.route)
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
        composable(Screen.Calendar.route) {
            CalendarScreen(
                onDaySelected = { date ->
                    navController.navigate(Screen.Workout.createRoute(date))
                },
                onHomeClick = {
                    navController.navigate(Screen.Start.route)
                },
                onHelpClick = {
                    navController.navigate(Screen.HelpMuscleGroups.route)
                }
            )
        }
        composable(Screen.HelpMuscleGroups.route) {
            HelpMuscleGroupsScreen(
                onMuscleGroupSelected = { category ->
                    navController.navigate(Screen.HelpExercises.createRoute(category))
                },
                onHomeClick = {
                    navController.navigate(Screen.Start.route)
                }
            )
        }
        composable(
            route = Screen.HelpExercises.route,
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: return@composable
            HelpExercisesScreen(
                category = category,
                onExerciseClick = { exercise ->
                    navController.navigate(Screen.ExerciseInstructions.createRoute(exercise))
                },
                onHomeClick = {
                    navController.navigate(Screen.Start.route)
                }
            )
        }
    }
} 