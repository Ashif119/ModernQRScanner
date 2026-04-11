package com.itandcstech.modernqrscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.itandcstech.modernqrscanner.presentation.NavGraph
import com.itandcstech.modernqrscanner.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint  // ← Yeh priority hai Hilt ke lie
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                // NavController = app ka remote control
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }

        }
    }
}
