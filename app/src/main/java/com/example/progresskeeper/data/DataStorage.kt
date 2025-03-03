package com.example.progresskeeper.data

import android.content.Context
import android.content.SharedPreferences
import com.example.progresskeeper.screens.ExerciseSet
import org.json.JSONArray
import org.json.JSONObject

class DataStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "workout_data", Context.MODE_PRIVATE
    )

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
} 