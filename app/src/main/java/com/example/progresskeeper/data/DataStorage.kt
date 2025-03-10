package com.example.progresskeeper.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "workout_data", Context.MODE_PRIVATE
    )
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun saveExerciseSets(exercise: String, sets: List<ExerciseSet>) {
        val jsonArray = JSONArray()
        sets.forEach { set ->
            val jsonObject = JSONObject()
            jsonObject.put("setNumber", set.setNumber)
            jsonObject.put("weight", set.weight)
            jsonObject.put("reps", set.reps)
            jsonArray.put(jsonObject)
        }
        
        val editor = sharedPreferences.edit()
        editor.putString("sets_$exercise", jsonArray.toString())
        editor.apply()
    }

    fun loadExerciseSets(exercise: String): List<ExerciseSet> {
        val jsonString = sharedPreferences.getString("sets_$exercise", null) ?: return emptyList()
        val jsonArray = JSONArray(jsonString)
        val sets = mutableListOf<ExerciseSet>()
        
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val setNumber = jsonObject.getInt("setNumber")
            val weight = jsonObject.getString("weight")
            val reps = jsonObject.getString("reps")
            sets.add(ExerciseSet(setNumber, weight, reps))
        }
        
        return sets
    }

    fun saveExerciseNames(category: String, exercises: List<String>) {
        val jsonArray = JSONArray()
        exercises.forEach { exercise ->
            jsonArray.put(exercise)
        }
        
        val editor = sharedPreferences.edit()
        editor.putString("exercises_$category", jsonArray.toString())
        editor.apply()
    }

    fun loadExerciseNames(category: String): List<String> {
        val jsonString = sharedPreferences.getString("exercises_$category", null) ?: return emptyList()
        val jsonArray = JSONArray(jsonString)
        val exercises = mutableListOf<String>()
        
        for (i in 0 until jsonArray.length()) {
            exercises.add(jsonArray.getString(i))
        }
        
        return exercises
    }

    fun saveWorkout(workout: Workout) {
        val jsonObject = JSONObject()
        jsonObject.put("date", dateFormat.format(workout.date))
        
        val exercisesArray = JSONArray()
        workout.exercises.forEach { exercise ->
            val exerciseObject = JSONObject()
            exerciseObject.put("name", exercise.name)
            
            val setsArray = JSONArray()
            exercise.sets.forEach { set ->
                val setObject = JSONObject()
                setObject.put("setNumber", set.setNumber)
                setObject.put("weight", set.weight)
                setObject.put("reps", set.reps)
                setsArray.put(setObject)
            }
            exerciseObject.put("sets", setsArray)
            exercisesArray.put(exerciseObject)
        }
        jsonObject.put("exercises", exercisesArray)
        
        val editor = sharedPreferences.edit()
        editor.putString("workout_${dateFormat.format(workout.date)}", jsonObject.toString())
        editor.apply()
    }

    fun loadWorkout(date: Date): Workout? {
        val jsonString = sharedPreferences.getString("workout_${dateFormat.format(date)}", null) 
            ?: return null
            
        val jsonObject = JSONObject(jsonString)
        val exercisesArray = jsonObject.getJSONArray("exercises")
        val exercises = mutableListOf<WorkoutExercise>()
        
        for (i in 0 until exercisesArray.length()) {
            val exerciseObject = exercisesArray.getJSONObject(i)
            val name = exerciseObject.getString("name")
            
            val setsArray = exerciseObject.getJSONArray("sets")
            val sets = mutableListOf<ExerciseSet>()
            
            for (j in 0 until setsArray.length()) {
                val setObject = setsArray.getJSONObject(j)
                sets.add(ExerciseSet(
                    setNumber = setObject.getInt("setNumber"),
                    weight = setObject.getString("weight"),
                    reps = setObject.getString("reps")
                ))
            }
            
            exercises.add(WorkoutExercise(name, sets))
        }
        
        return Workout(date, exercises)
    }

    fun hasWorkoutForDate(date: Date): Boolean {
        return sharedPreferences.contains("workout_${dateFormat.format(date)}")
    }
} 