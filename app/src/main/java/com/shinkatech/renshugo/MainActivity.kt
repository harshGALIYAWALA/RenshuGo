package com.shinkatech.renshugo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            Scaffold(modifier = Modifier.fillMaxSize()) {
//                var showSplash by remember { mutableStateOf(true) }
//                if (showSplash) {
//                    SplashScreen(
//                        modifier = Modifier.padding(it),
//                        onTimeOut = { showSplash = false },
//                    )
//                } else {
//                    AccessKana(
//                        modifier = Modifier.padding(it)
//                    )
//                }
//            }

            val navController = rememberNavController()
            Navigation(navController)
        }
    }
}

data class HiraganaModel(
    val audio: String,
    val category: String,
    val example1_en: String,
    val example1_jp: String,
    val example1_roumaji: String,
    val example2_en: String,
    val example2_jp: String,
    val example2_roumaji: String,
    val id: Int,
    val image: String,
    val kana: String,
    val roumaji: String,
    val strokeOrder: String,
    val type: String
)

@Composable
fun AccessKana(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hiragana by remember { mutableStateOf<List<HiraganaModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val json = context.assets.open("hiragana/hiragana.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<HiraganaModel>>() {}.type
            hiragana = Gson().fromJson(json, type)
        } catch (e: Exception) {
            Log.e("AccessKana", "Error reading JSON file", e)
            e.printStackTrace()
        }
    }

    // Show in LazyColumn
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(hiragana) { kana ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = kana.kana, style = MaterialTheme.typography.headlineMedium)
                    Text(text = kana.roumaji, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Category: ${kana.category}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}