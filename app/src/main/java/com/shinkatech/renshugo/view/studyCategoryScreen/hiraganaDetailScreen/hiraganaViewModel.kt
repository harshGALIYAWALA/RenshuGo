package com.shinkatech.renshugo.view.studyCategoryScreen.hiraganaDetailScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// UI State for the screen
data class HiraganaUiState(
    val kanaList: List<Kana> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class HiraganaViewModel : ViewModel() {

    // Private mutable state flow
    private val _uiState = MutableStateFlow(HiraganaUiState())
    // Public read-only state flow
    val uiState: StateFlow<HiraganaUiState> = _uiState.asStateFlow()

    /**
     * Load hiragana data from JSON file
     * @param context Android context to access assets
     * @param fileName JSON file name in assets folder
     */
    fun loadHiraganaData(context: Context, fileName: String = "hiragana/hiragana.json") {
        viewModelScope.launch {
            try {
                // Set loading state
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                // Load and parse JSON data
                val kanaList = loadKanaListFromAssets(context, fileName)

                // Update state with loaded data
                _uiState.value = _uiState.value.copy(
                    kanaList = kanaList,
                    isLoading = false
                )

            } catch (e: Exception) {
                // Handle error state
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load hiragana data: ${e.message}"
                )
            }
        }
    }

    /**
     * Private function to load JSON from assets and parse to List<Kana>
     */
    private fun loadKanaListFromAssets(context: Context, fileName: String): List<Kana> {
        val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Kana>>() {}.type
        return Gson().fromJson(json, type)
    }

    /**
     * Get kana character by ID
     */
    fun getKanaById(id: Int): Kana? {
        return _uiState.value.kanaList.find { it.id == id }
    }

    /**
     * Get chunked list for grid display (5 items per row)
     */
    fun getKanaListInRows(): List<List<Kana>> {
        return _uiState.value.kanaList.chunked(5)
    }
}