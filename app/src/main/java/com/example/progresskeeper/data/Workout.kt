package com.example.progresskeeper.data

import java.util.Date

data class Workout(
    val date: Date,
    val exercises: List<WorkoutExercise>
)

data class WorkoutExercise(
    val name: String,
    val sets: List<ExerciseSet>
) 