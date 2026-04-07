package com.itandcstech.modernqrscanner.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itandcstech.modernqrscanner.presentation.camera.CameraScreen
import com.itandcstech.modernqrscanner.presentation.home.HomeScreen
import com.itandcstech.modernqrscanner.presentation.splash.SplashScreen

/**
 * @Created by Ashif on 07-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route)
                }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}