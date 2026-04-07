package com.itandcstech.modernqrscanner.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * @Created by Ashif on 07-04-2026
 */
@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {

    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "splash_alpha"
    )

    // ✅ scaleAnim ab use ho raha hai
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1200),
        label = "splash_scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // ✅ Glow — sirf ek baar, Box ke andar
        Box(
            modifier = Modifier
                .size(300.dp)
                .blur(80.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alphaAnim)
                .scale(scaleAnim)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // QR Logo Container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "QR Logo",
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // App Name
            Text(
                text = "SCANNER",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 8.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Scan • Save • Share",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 4.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }

        // "Powered by ML Kit" — Column ke bahar, Box ke andar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alphaAnim)
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Powered by ML Kit",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun SplashScreenPreview() {
    // Replace 'ModernQRScannerTheme' with your actual project theme name
    MaterialTheme {
        SplashScreen(onNavigateToHome = {})
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun SplashScreenDarkPreview() {
    MaterialTheme {
        SplashScreen(onNavigateToHome = {})
    }
}


