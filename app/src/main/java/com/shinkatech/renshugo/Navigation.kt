package com.shinkatech.renshugo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shinkatech.renshugo.view.kanaDetailScreen.KanaDetailScreen
import com.shinkatech.renshugo.view.mainScreen.MainScreen
import com.shinkatech.renshugo.view.selectLevel.SelectLevel
import com.shinkatech.renshugo.view.splashScreen.SplashScreen

sealed class Screen(val route: String){
    object SplashScreen: Screen("splash_screen")
    object SelectLevel: Screen("select_level")
    object MainScreen: Screen("main_screen")
    object KanaDetailScreen: Screen("Kana_detail_screen")

}

@Composable
fun Navigation(navController1: NavHostController) {
    // Use the passed navController instead of creating a new one
    // This ensures consistency across your app
    NavHost(
        navController = navController1, // Use the passed controller
        startDestination = Screen.SplashScreen.route
    ){
        composable(Screen.SplashScreen.route){
            SplashScreen(navController1)
        }
        composable(Screen.SelectLevel.route){
            SelectLevel(navController1)
        }
        composable(Screen.MainScreen.route) {
            MainScreen(navController1)
        }
        composable(Screen.KanaDetailScreen.route) {
            KanaDetailScreen(navController1)
        }
    }
}