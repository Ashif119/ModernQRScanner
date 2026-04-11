package com.itandcstech.modernqrscanner.presentation.camera

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * @Created by Ashif on 07-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */

@Composable
fun CameraScreen(
    onNavigateBack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isTorchOn by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Camera Permission Check ──
        CameraPermissionHandler {
            // ── Camera Preview — Full Screen ──
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                isTorchOn = isTorchOn,
                onQRCodeDetected = { content ->
                    viewModel.onQRCodeDetected(content)
                }
            )
        }

        // ── Dark Overlay ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        // ── Main Content ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Top Bar
            CameraTopBar(
                isTorchOn = isTorchOn,
                onBackClick = onNavigateBack,
                onTorchClick = { isTorchOn = !isTorchOn }
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Scanner Frame
            ScannerFrame()

            Spacer(modifier = Modifier.weight(0.3f))

            // Align Text
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ALIGN QR CODE",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Position the code within the frame to scan\nautomatically. Support for QR, Data Matrix, and Barcodes.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Bottom Controls
            CameraBottomControls(
                onNavigateBack = onNavigateBack
            )
        }

        // ── Result Bottom Sheet ──
        AnimatedVisibility(
            visible = uiState.scannedContent != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            uiState.scannedContent?.let { content ->
                ScanResultSheet(
                    content = content,
                    type = uiState.scannedType ?: "TEXT",
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(content))
                    },
                    onScanAgain = {
                        viewModel.resetScanner()
                    }
                )
            }
        }
    }
}

// ─────────────────────────────────────────
// Camera Top Bar
// ─────────────────────────────────────────
@Composable
fun CameraTopBar(
    isTorchOn: Boolean,
    onBackClick: () -> Unit,
    onTorchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back Button
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.QrCodeScanner,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "SCANNER",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Torch Button
        IconButton(onClick = onTorchClick) {
            Icon(
                imageVector = if (isTorchOn)
                    Icons.Outlined.FlashOn
                else
                    Icons.Outlined.FlashOff,
                contentDescription = "Torch",
                tint = if (isTorchOn)
                    MaterialTheme.colorScheme.primary
                else
                    Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ─────────────────────────────────────────
// Scanner Frame — Corner brackets + Glow
// ─────────────────────────────────────────
@Composable
fun ScannerFrame() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(280.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .size(200.dp)
                .blur(60.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Corner Brackets
        val primary = MaterialTheme.colorScheme.primary
        val strokeWidth = 3.dp
        val bracketSize = 36.dp
        val padding = 0.dp

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            // Top-left
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Box(modifier = Modifier
                    .width(bracketSize).height(strokeWidth)
                    .background(primary))
                Box(modifier = Modifier
                    .width(strokeWidth).height(bracketSize)
                    .background(primary))
            }
            // Top-right
            Column(modifier = Modifier.align(Alignment.TopEnd)) {
                Box(modifier = Modifier
                    .width(bracketSize).height(strokeWidth)
                    .background(primary))
                Box(modifier = Modifier
                    .width(strokeWidth).height(bracketSize)
                    .align(Alignment.End)
                    .background(primary))
            }
            // Bottom-left
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Box(modifier = Modifier
                    .width(strokeWidth).height(bracketSize)
                    .background(primary))
                Box(modifier = Modifier
                    .width(bracketSize).height(strokeWidth)
                    .background(primary))
            }
            // Bottom-right
            Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                Box(modifier = Modifier
                    .width(strokeWidth).height(bracketSize)
                    .align(Alignment.End)
                    .background(primary))
                Box(modifier = Modifier
                    .width(bracketSize).height(strokeWidth)
                    .background(primary))
            }
        }
    }
}

// ─────────────────────────────────────────
// Camera Bottom Controls
// ─────────────────────────────────────────
@Composable
fun CameraBottomControls(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(26.dp)
            )
        }

        // Scan Button — Center
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.QrCodeScanner,
                contentDescription = "Scan",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(30.dp)
            )
        }

        // Placeholder for symmetry
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.QrCodeScanner,
                contentDescription = null,
                tint = Color.Transparent,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

// ─────────────────────────────────────────
// Scan Result Bottom Sheet
// ─────────────────────────────────────────
@Composable
fun ScanResultSheet(
    content: String,
    type: String,
    onCopy: () -> Unit,
    onScanAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // Handle bar
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outline)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Header row — Type badge + icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Type Icon Circle
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (type.uppercase()) {
                        "URL" -> Icons.Outlined.OpenInBrowser
                        "EMAIL" -> Icons.Outlined.ContentCopy
                        else -> Icons.Outlined.QrCodeScanner
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "QR Code Detected!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Type badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Content Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(14.dp)
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCopy,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = "Copy",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Copy",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onScanAgain,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "Scan Again",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Scan Again",
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}