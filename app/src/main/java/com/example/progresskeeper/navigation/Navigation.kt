package com.example.progresskeeper.navigation

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object WorkoutCategories : Screen("workout_categories")
    object Exercises : Screen("exercises/{category}") {
        fun createRoute(category: String) = "exercises/$category"
    }
} 