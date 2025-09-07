package com.shinkatech.renshugo.view.studyCategoryScreen.hiraganaDetailScreen

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

data class Kana(
    val id: Int,
    val kana: String,
    val roumaji: String,
    val type: String,
    val category: String,
    val audio: String,
    val strokeOrder: String,
    val image: String,
    val example1_jp: String,
    val example1_roumaji: String,
    val example1_en: String,
    val example2_jp: String,
    val example2_roumaji: String,
    val example2_en: String
)

data class KanaGroup(
    val title: String,
    val description: String,
    val kanaList: List<Kana>,
    val color: Color
)

// UI State management
data class HiraganaDetailUiState(
    val isLoading: Boolean = true,
    val kanaGroups: List<KanaGroup> = emptyList(),
    val error: String? = null,
    val expandedGroups: Set<Int> = setOf(0, 1, 2, 3) // All expanded by default
)

// ViewModel for better state management
class HiraganaDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HiraganaDetailUiState())
    val uiState: StateFlow<HiraganaDetailUiState> = _uiState.asStateFlow()

    // Cache for loaded data
    private var cachedKanaList: List<Kana>? = null
    private var cachedGroups: List<KanaGroup>? = null

    suspend fun loadKanaData(context: Context) {
        if (cachedGroups != null) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                kanaGroups = cachedGroups!!
            )
            return
        }

        try {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val kanaList = loadKanaListAsync(context, "hiragana/hiragana.json")
            val groups = groupKanaByType(kanaList)

            // Cache the results
            cachedKanaList = kanaList
            cachedGroups = groups

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                kanaGroups = groups
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = e.message ?: "Unknown error occurred"
            )
        }
    }

    fun toggleGroupExpansion(groupIndex: Int) {
        val currentExpanded = _uiState.value.expandedGroups.toMutableSet()
        if (currentExpanded.contains(groupIndex)) {
            currentExpanded.remove(groupIndex)
        } else {
            currentExpanded.add(groupIndex)
        }
        _uiState.value = _uiState.value.copy(expandedGroups = currentExpanded)
    }
}

// Async functions with proper coroutine usage
suspend fun loadJsonFromAssetsAsync(context: Context, fileName: String): String =
    withContext(Dispatchers.IO) {
        context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

suspend fun loadKanaListAsync(context: Context, fileName: String): List<Kana> =
    withContext(Dispatchers.IO) {
        val json = loadJsonFromAssetsAsync(context, fileName)
        val type = object : TypeToken<List<Kana>>() {}.type
        Gson().fromJson(json, type)
    }

// Memoized color list
private val groupColors = listOf(
    Color(0xFF667EEA),
    Color(0xFFFF6B6B),
    Color(0xFF4ECDC4),
    Color(0xFFFFA726),
    Color(0xFFAB47BC),
    Color(0xFF26C6DA)
)

fun groupKanaByType(kanaList: List<Kana>): List<KanaGroup> {
    val groups = mutableListOf<KanaGroup>()

    // Basic Hiragana (あ-ん)
    val basicKana = kanaList.filter { it.type == "gojuuon" && it.id <= 71 }
    if (basicKana.isNotEmpty()) {
        groups.add(
            KanaGroup(
                title = "基本ひらがな (Basic Hiragana)",
                description = "46 basic hiragana characters",
                kanaList = basicKana,
                color = groupColors[0]
            )
        )
    }

    // Dakuten (が-ぞ, だ-ど, ば-ぼ)
    val dakutenKana = kanaList.filter { it.type == "dakuten" }
    if (dakutenKana.isNotEmpty()) {
        groups.add(
            KanaGroup(
                title = "濁音 (Dakuten)",
                description = "Voiced sounds with \" marks",
                kanaList = dakutenKana,
                color = groupColors[1]
            )
        )
    }

    // Handakuten (ぱ-ぽ)
    val handakutenKana = kanaList.filter { it.type == "handakuten" }
    if (handakutenKana.isNotEmpty()) {
        groups.add(
            KanaGroup(
                title = "半濁音 (Handakuten)",
                description = "Semi-voiced sounds with ° marks",
                kanaList = handakutenKana,
                color = groupColors[2]
            )
        )
    }

    // Combination sounds (きゃ, しゃ, etc.)
    val combinationKana = kanaList.filter { it.type == "combination" }
    if (combinationKana.isNotEmpty()) {
        groups.add(
            KanaGroup(
                title = "拗音 (Combination Sounds)",
                description = "Two-character combinations",
                kanaList = combinationKana,
                color = groupColors[3]
            )
        )
    }

    return groups
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HiraganaDetailScreen(
    navController1: NavHostController,
    viewModel: HiraganaDetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Load data on first composition
    LaunchedEffect(Unit) {
        viewModel.loadKanaData(context)
    }

    // Define gradient colors (memoized)
    val backgroundGradient = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF8F9FE),
                Color(0xFFF0F4FF)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "ひらがな学習",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D3436)
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF667EEA)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading hiragana...",
                                color = Color(0xFF636E72)
                            )
                        }
                    }
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error: ${uiState.error}",
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    // Retry loading - you can implement retry logic here
                                }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(
                            items = uiState.kanaGroups,
                            key = { group -> group.title }
                        ) { group ->
                            val groupIndex = uiState.kanaGroups.indexOf(group)
                            KanaGroupSection(
                                group = group,
                                isExpanded = uiState.expandedGroups.contains(groupIndex),
                                onToggleExpansion = { viewModel.toggleGroupExpansion(groupIndex) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KanaGroupSection(
    group: KanaGroup,
    isExpanded: Boolean,
    onToggleExpansion: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Group Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpansion() }
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = group.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = group.color
                    )
                    Text(
                        text = group.description,
                        fontSize = 12.sp,
                        color = Color(0xFF636E72),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Character count badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = group.color.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "${group.kanaList.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = group.color,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = group.color
                    )
                }
            }

            // Collapsible content with AnimatedVisibility for smoother transitions
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                // Use proper LazyVerticalGrid without fixed height for better performance
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.height((72.dp * ((group.kanaList.size + 3) / 4))), // Dynamic height calculation
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false
                ) {
                    items(
                        items = group.kanaList,
                        key = { kana -> kana.id }
                    ) { kana ->
                        KanaGridButton(
                            kana = kana,
                            groupColor = group.color
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KanaGridButton(
    kana: Kana,
    groupColor: Color,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    // Memoize the gradient to prevent recreation on every recomposition
    val cardGradient = remember(groupColor) {
        Brush.verticalGradient(
            colors = listOf(
                groupColor.copy(alpha = 0.08f),
                groupColor.copy(alpha = 0.04f)
            )
        )
    }

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                // Handle click - navigate to detail screen or play audio
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardGradient)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Main Hiragana character
                Text(
                    text = kana.kana,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D3436),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Romaji
                Text(
                    text = kana.roumaji,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    color = groupColor,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.1.sp
                )
            }
        }
    }
}