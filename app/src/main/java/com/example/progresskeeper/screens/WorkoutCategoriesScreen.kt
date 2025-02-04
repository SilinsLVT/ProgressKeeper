package com.example.progresskeeper.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WorkoutCategoriesScreen(onCategoryClick: (String) -> Unit) {
    val categories = listOf(
        "Traps",
        "Shoulders",
        "Chest",
        "Back",
        "Triceps",
        "Biceps",
        "Forearms",
        "Legs",
        "Calves"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            Button(
                onClick = { onCategoryClick(category) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = category)
            }
        }
    }
} 