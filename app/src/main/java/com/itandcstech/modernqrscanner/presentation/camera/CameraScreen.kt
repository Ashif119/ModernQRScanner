package com.itandcstech.modernqrscanner.presentation.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * @Created by Ashif on 07-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
@Composable
fun CameraScreen(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Camera Screen")  // Baad mein design karenge
    }
}