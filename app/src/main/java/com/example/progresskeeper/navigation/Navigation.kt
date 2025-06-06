package com.example.progresskeeper.navigation

import java.util.Date

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object Home : Screen("home")
    object WorkoutCategories : Screen("workout_categories")
    object Exercises : Screen("exercises/{category}") {
        fun createRoute(category: String) = "exercises/$category"
    }
    object ExerciseDetails : Screen("exercise_details/{exercise}") {
        fun createRoute(exercise: String) = "exercise_details/$exercise"
    }
    object Workout : Screen("workout/{date}") {
        fun createRoute(date: Date) = "workout/${date.time}"
    }
    object Calendar : Screen("calendar")
    object HelpMuscleGroups : Screen("help_muscle_groups")
    object HelpExercises : Screen("help_exercises/{category}") {
        fun createRoute(category: String) = "help_exercises/$category"
    }
    object ExerciseInstructions : Screen("exercise_instructions/{exercise}") {
        fun createRoute(exercise: String) = "exercise_instructions/$exercise"
    }
    object WeeklyReport : Screen("weekly_report")
} 