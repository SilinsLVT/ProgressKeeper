package com.example.progresskeeper.navigation

import java.util.Date

sealed class Screen(val route: String) {
    object Start : Screen("start")
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
    object Calendar : Screen("calendar") {
        fun createRoute() = "calendar"
    }
} 