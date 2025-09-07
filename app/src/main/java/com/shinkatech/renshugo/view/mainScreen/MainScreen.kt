package com.shinkatech.renshugo.view.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shinkatech.renshugo.Screen

@Composable
fun MainScreen(navController: NavHostController) {
    // Memoize static data to prevent recreating on every recomposition
    val studyCategories = remember { getStudyCategoryItems() }
    val overallProgress = remember { 0.3f }
    val wordsLearned = remember { 450 }
    val totalWords = remember { 1500 }
    val wordsRemaining = remember { totalWords - wordsLearned }
    val overallPercentage = remember(overallProgress) { (overallProgress * 100).toInt() }
    val dailyGoalProgress = remember { "12/20" }

    // Memoize icons to prevent recreation
    val starIcon = remember { Icons.Default.Star }
    val arrowIcon = remember { Icons.AutoMirrored.Filled.KeyboardArrowRight }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "header") {
                // Header Section - optimized with reduced nesting
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "JLPT N5 Progress",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Keep going! You're doing great 🌸",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item(key = "overall_progress") {
                // Overall Progress Card - optimized with precomputed values
                Card(
                    onClick = {
                        // Navigate to detailed progress view
                    },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Overall Progress",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "$overallPercentage%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Progress indicator - simplified structure
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LinearProgressIndicator(
                                progress = { overallProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "$wordsLearned / $totalWords words learned",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$wordsRemaining remaining",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            item(key = "writing_systems_header") {
                Text(
                    text = "Writing Systems",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item(key = "writing_systems") {
                // Writing Systems Cards - optimized with Box + background instead of Surface
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Hiragana Card
                    WritingSystemCard(
                        modifier = Modifier.weight(1f),
                        character = "あ",
                        name = "Hiragana",
                        description = "46 characters",
                        color = Color(0xFF6B73FF),
                        onClick = {
                            navController.navigate(Screen.hiraganaDetailScreen.route)
                        }
                    )

                    // Katakana Card
                    WritingSystemCard(
                        modifier = Modifier.weight(1f),
                        character = "ア",
                        name = "Katakana",
                        description = "46 characters",
                        color = Color(0xFF9C27B0),
                        onClick = {
//                            navController.navigate("kana_detail/katakana")
                        }
                    )
                }
            }

            item(key = "study_categories_header") {
                Text(
                    text = "Study Categories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(
                items = studyCategories,
                key = { it.name } // Stable key for better recycling
            ) { category ->
                CategoryProgressCard(
                    category = category,
                    onClick = {
                        // Navigate to specific category
                    }
                )
            }

            item(key = "daily_goal") {
                // Daily Goal Card - simplified with spacing optimization
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = starIcon,
                            contentDescription = "Daily Goal",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Today's Goal",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Study 20 new words",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = dailyGoalProgress,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WritingSystemCard(
    modifier: Modifier = Modifier,
    character: String,
    name: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Character Icon - optimized with Box + background instead of Surface
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = character,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }

            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CategoryProgressCard(
    category: CategoryItem,
    onClick: () -> Unit
) {
    // Precompute progress percentage to avoid calculation in UI
    val progressPercentage = remember(category.progress) {
        (category.progress * 100).toInt()
    }

    val arrowIcon = remember { Icons.AutoMirrored.Filled.KeyboardArrowRight }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category Icon - optimized with Box + background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(category.color.copy(alpha = 0.1f))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = category.color,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Category Info - optimized spacing and precomputed strings
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${category.completed}/${category.total} completed",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LinearProgressIndicator(
                    progress = { category.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = category.color,
                    trackColor = category.color.copy(alpha = 0.2f)
                )
            }

            // Progress Percentage - using precomputed value
            Text(
                text = "$progressPercentage%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = category.color
            )

            Icon(
                imageVector = arrowIcon,
                contentDescription = "View details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Data classes remain the same
data class CategoryItem(
    val name: String,
    val completed: Int,
    val total: Int,
    val progress: Float,
    val icon: ImageVector,
    val color: Color
)

data class WritingSystemItem(
    val name: String,
    val status: String,
    val icon: ImageVector,
    val color: Color
)

fun getHiraganaItem(): WritingSystemItem {
    return WritingSystemItem(
        name = "Hiragana",
        status = "Master the basics",
        icon = Icons.Default.Create,
        color = Color(0xFF6B73FF)
    )
}

fun getKatakanaItem(): WritingSystemItem {
    return WritingSystemItem(
        name = "Katakana",
        status = "Master the basics",
        icon = Icons.Default.Edit,
        color = Color(0xFF9C27B0)
    )
}

fun getStudyCategoryItems(): List<CategoryItem> {
    return listOf(
        CategoryItem(
            name = "Vocabulary",
            completed = 450,
            total = 800,
            progress = 0.56f,
            icon = Icons.Default.MenuBook,
            color = Color(0xFF6B73FF)
        ),
        CategoryItem(
            name = "Grammar",
            completed = 15,
            total = 80,
            progress = 0.19f,
            icon = Icons.Default.School,
            color = Color(0xFF00C896)
        ),
        CategoryItem(
            name = "Kanji",
            completed = 45,
            total = 103,
            progress = 0.44f,
            icon = Icons.Default.Translate,
            color = Color(0xFFFF6B6B)
        ),
        CategoryItem(
            name = "Reading",
            completed = 8,
            total = 25,
            progress = 0.32f,
            icon = Icons.Default.AutoStories,
            color = Color(0xFFFFBE0B)
        ),
        CategoryItem(
            name = "Listening",
            completed = 5,
            total = 20,
            progress = 0.25f,
            icon = Icons.Default.Headphones,
            color = Color(0xFF8338EC)
        )
    )
}