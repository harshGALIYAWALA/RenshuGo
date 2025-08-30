package com.shinkatech.renshugo.view.splashScreen


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.shinkatech.renshugo.R
import com.shinkatech.renshugo.Screen

@Composable
fun SplashScreen(
    navController: NavController
){
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(1200))
        delay(1500)
        navController.navigate(Screen.SelectLevel.route)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.main_logo),
                contentDescription = "Main Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer{this.alpha = alpha.value}
            )
        }
    }
}