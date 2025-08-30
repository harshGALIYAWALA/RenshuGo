package com.shinkatech.renshugo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shinkatech.renshugo.view.selectLevel.SelectLevel
import com.shinkatech.renshugo.view.splashScreen.SplashScreen


sealed class Screen(val route: String){
    object SplashScreen: Screen("splash_screen")
    object SelectLevel: Screen("select_level")
}

@Composable
fun Navigation(navController1: NavHostController) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ){
        composable(Screen.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(Screen.SelectLevel.route){
            SelectLevel(navController)
        }

    }

}