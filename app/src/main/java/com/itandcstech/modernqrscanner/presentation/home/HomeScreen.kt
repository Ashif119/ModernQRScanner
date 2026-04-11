package com.itandcstech.modernqrscanner.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.itandcstech.modernqrscanner.domain.model.QRResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @Created by Ashif on 07-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */

@Composable
fun HomeScreen(
    onNavigateToCamera: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top Bar
            HomeTopBar()

            // Scrollable Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    ScannerBox(onNavigateToCamera = onNavigateToCamera)
                    Spacer(modifier = Modifier.height(28.dp))
                    ReadyTitle()
                    Spacer(modifier = Modifier.height(28.dp))
                    RecentActivityHeader(count = uiState.scanHistory.size)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (uiState.scanHistory.isEmpty()) {
                    item { EmptyHistoryItem() }
                } else {
                    items(uiState.scanHistory.take(10)) { result ->
                        ScanHistoryItem(result = result)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        // Bottom Navigation Bar
        BottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onScanClick = onNavigateToCamera
        )
    }
}

// ─────────────────────────────────────────
// Top Bar
// ─────────────────────────────────────────
@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo + Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_camera),
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SCANNER",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 3.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        // History Icon
        IconButton(onClick = { }) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
                contentDescription = "History",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ─────────────────────────────────────────
// Scanner Box — Corner brackets
// ─────────────────────────────────────────
@Composable
fun ScannerBox(onNavigateToCamera: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onNavigateToCamera() },
        contentAlignment = Alignment.Center
    ) {
        // Glow behind button
        Box(
            modifier = Modifier
                .size(160.dp)
                .blur(40.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Green scan button
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "Scan",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TAP TO SCAN",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 3.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Corner brackets
        CornerBrackets()
    }
}

@Composable
fun CornerBrackets() {
    val color = MaterialTheme.colorScheme.primary
    val strokeWidth = 3.dp
    val bracketSize = 32.dp
    val padding = 16.dp

    // Top Left
    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        // Top-left
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            Box(modifier = Modifier
                .width(bracketSize)
                .height(strokeWidth)
                .background(color))
            Box(modifier = Modifier
                .width(strokeWidth)
                .height(bracketSize)
                .background(color))
        }
        // Top-right
        Column(modifier = Modifier.align(Alignment.TopEnd)) {
            Box(modifier = Modifier
                .width(bracketSize)
                .height(strokeWidth)
                .background(color))
            Box(modifier = Modifier
                .width(strokeWidth)
                .height(bracketSize)
                .align(Alignment.End)
                .background(color))
        }
        // Bottom-left
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Box(modifier = Modifier
                .width(strokeWidth)
                .height(bracketSize)
                .background(color))
            Box(modifier = Modifier
                .width(bracketSize)
                .height(strokeWidth)
                .background(color))
        }
        // Bottom-right
        Column(modifier = Modifier.align(Alignment.BottomEnd)) {
            Box(modifier = Modifier
                .width(strokeWidth)
                .height(bracketSize)
                .align(Alignment.End)
                .background(color))
            Box(modifier = Modifier
                .width(bracketSize)
                .height(strokeWidth)
                .background(color))
        }
    }
}

// ─────────────────────────────────────────
// Ready Title
// ─────────────────────────────────────────
@Composable
fun ReadyTitle() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ready for Data",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Align any QR code within the frame to decode",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ─────────────────────────────────────────
// Recent Activity Header
// ─────────────────────────────────────────
@Composable
fun RecentActivityHeader(count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "RECENT ACTIVITY",
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$count total",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─────────────────────────────────────────
// Scan History Item
// ─────────────────────────────────────────
@Composable
fun ScanHistoryItem(result: QRResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Type Icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getQRTypeIcon(result.type),
                contentDescription = result.type,
                tint = getQRTypeColor(result.type),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Content + Time
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = result.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(
                        id = android.R.drawable.ic_menu_recent_history
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${formatTime(result.timestamp)} • ${result.type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // Arrow
        Icon(
            painter = painterResource(
                id = android.R.drawable.ic_media_next
            ),
            contentDescription = "Open",
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(16.dp)
        )
    }
}

// ─────────────────────────────────────────
// Empty State
// ─────────────────────────────────────────
@Composable
fun EmptyHistoryItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No scans yet\nTap the button to scan your first QR!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// ─────────────────────────────────────────
// Bottom Navigation Bar
// ─────────────────────────────────────────
@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    onScanClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(bottom = 24.dp, top = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Scan Button — Center green
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onScanClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "Scan",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(30.dp)
                )
            }

            // Settings
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────
// Helper Functions
// ─────────────────────────────────────────
@Composable
fun getQRTypeIcon(type: String): ImageVector {
    return when (type.uppercase()) {
        "URL" -> Icons.Default.LocationOn
        "EMAIL" -> Icons.Default.Email
        "WIFI" -> Icons.Default.Add
        else -> Icons.Default.Email
    }
}

@Composable
fun getQRTypeColor(type: String): Color {
    return when (type.uppercase()) {
        "URL" -> MaterialTheme.colorScheme.primary
        "EMAIL" -> Color(0xFFFF6B6B)
        "WIFI" -> Color(0xFF4FC3F7)
        else -> MaterialTheme.colorScheme.primary
    }
}

fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000} min ago"
        diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
        else -> SimpleDateFormat("dd MMM", Locale.getDefault())
            .format(Date(timestamp))
    }
}
