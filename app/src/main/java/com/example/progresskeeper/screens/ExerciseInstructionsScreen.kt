package com.example.progresskeeper.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExerciseInstructionsScreen(
    exercise: String,
    onHomeClick: () -> Unit
) {
    val instructions = when (exercise) {
        "Barbell Shrugs" -> listOf(
            "Stand with your feet shoulder-width apart",
            "Hold a barbell in front of your thighs with an overhand grip",
            "Keep your arms straight and shoulders relaxed",
            "Lift your shoulders straight up toward your ears",
            "Hold the contraction for a second",
            "Lower the weight back down slowly"
        )
        "Dumbbell Shrugs" -> listOf(
            "Stand with your feet shoulder-width apart",
            "Hold a dumbbell in each hand at your sides",
            "Keep your arms straight and shoulders relaxed",
            "Lift your shoulders straight up toward your ears",
            "Hold the contraction for a second",
            "Lower the weights back down slowly"
        )
        "Behind-the-Back Smith Machine Shrugs" -> listOf(
            "Stand with your back to the Smith machine",
            "Grab the bar behind your back with an overhand grip",
            "Keep your arms straight and shoulders relaxed",
            "Lift your shoulders straight up toward your ears",
            "Hold the contraction for a second",
            "Lower the weight back down slowly"
        )
        "Rack Pulls" -> listOf(
            "Set up a barbell in a power rack at mid-thigh height",
            "Stand close to the bar with your feet shoulder-width apart",
            "Bend at your hips and knees to grab the bar",
            "Keep your back straight and chest up",
            "Pull the bar up by extending your hips and knees",
            "Squeeze your shoulder blades together at the top",
            "Lower the bar back down slowly"
        )
        "Face Pulls" -> listOf(
            "Attach a rope handle to a high pulley",
            "Stand facing the machine with your feet shoulder-width apart",
            "Grab the rope with both hands, palms facing each other",
            "Pull the rope toward your face, separating your hands as you pull",
            "Keep your elbows high and out to the sides",
            "Squeeze your shoulder blades together at the end of the movement",
            "Slowly return to the starting position"
        )
        else -> listOf("Instructions coming soon...")
    }
    
    val commonMistakes = when (exercise) {
        "Barbell Shrugs" -> listOf(
            "Using momentum to lift the weight",
            "Rolling the shoulders instead of lifting straight up",
            "Bending the elbows during the movement",
            "Not maintaining proper posture"
        )
        "Dumbbell Shrugs" -> listOf(
            "Using momentum to lift the weights",
            "Rolling the shoulders instead of lifting straight up",
            "Bending the elbows during the movement",
            "Not maintaining proper posture"
        )
        "Behind-the-Back Smith Machine Shrugs" -> listOf(
            "Using momentum to lift the weight",
            "Rolling the shoulders instead of lifting straight up",
            "Bending the elbows during the movement",
            "Not maintaining proper posture"
        )
        "Rack Pulls" -> listOf(
            "Rounding the back during the lift",
            "Using too much weight and compromising form",
            "Not engaging the core properly",
            "Not fully extending at the top of the movement"
        )
        "Face Pulls" -> listOf(
            "Pulling the rope too low",
            "Not separating the hands enough at the end of the movement",
            "Using momentum to pull the weight",
            "Not maintaining proper posture"
        )
        else -> listOf("Common mistakes coming soon...")
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = exercise,
            onCalendarClick = {},
            onAddClick = {},
            onHelpClick = {},
            onHomeClick = onHomeClick
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Instructions",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            
            items(instructions.size) { index ->
                Text(
                    text = "${index + 1}. ${instructions[index]}",
                    fontSize = 16.sp
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Common Mistakes",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            
            items(commonMistakes.size) { index ->
                Text(
                    text = "${index + 1}. ${commonMistakes[index]}",
                    fontSize = 16.sp
                )
            }
        }
    }
} 