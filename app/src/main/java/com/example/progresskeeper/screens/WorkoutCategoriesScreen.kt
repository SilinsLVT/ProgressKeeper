package com.example.progresskeeper.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkoutCategoriesScreen(
    onCategorySelected: (String) -> Unit,
    onHomeClick: () -> Unit,
    onHelpClick: () -> Unit
) {
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
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = "Muscle Groups",
            onCalendarClick = {},
            onAddClick = {},
            onHelpClick = onHelpClick,
            onHomeClick = onHomeClick
        )
        
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            text = category,
            fontSize = 16.sp
        )
    }
} 