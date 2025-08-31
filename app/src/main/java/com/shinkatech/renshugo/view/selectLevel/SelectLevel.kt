package com.shinkatech.renshugo.view.selectLevel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.shinkatech.renshugo.Screen

data class JLPTLevel(
    val name: String,
    val level: String,
    val isAvailable: Boolean
)

@Composable
fun SelectLevel(navController: NavHostController) {
    val levelList = listOf(
        JLPTLevel("JLPT Level N5", "N5", true),
        JLPTLevel("JLPT Level N4", "N4", false),
        JLPTLevel("JLPT Level N3", "N3", false),
        JLPTLevel("JLPT Level N2", "N2", false),
        JLPTLevel("JLPT Level N1", "N1", false)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Header Section
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Choose Your Level",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Start your Japanese learning journey",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Level Cards
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(levelList) { level ->
                    LevelCard(
                        level = level,
                        onClick = {
                            if (level.isAvailable) {
                                // for now not using DataStore to saved Jlpt level
                                navController.navigate(Screen.MainScreen.route){
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun LevelCard(
    level: JLPTLevel,
    onClick: () -> Unit
) {
    Card(
        onClick = {
            if (level.isAvailable) onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (level.isAvailable) 1f else 0.6f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (level.isAvailable) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (level.isAvailable)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = level.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (level.isAvailable)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (level.isAvailable) {
                        Text(
                            text = "Ready to start",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    } else {
                        Surface(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Coming Soon",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Level Badge
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = if (level.isAvailable)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = level.level,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (level.isAvailable)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}