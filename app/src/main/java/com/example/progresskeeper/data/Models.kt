package com.example.progresskeeper.data

data class ExerciseSet(
    val setNumber: Int,
    val weight: String,
    val reps: String
)

data class WorkoutExercise(
    val name: String,
    val sets: List<ExerciseSet>
)

data class Workout(
    val date: java.util.Date,
    val exercises: List<WorkoutExercise>
) 