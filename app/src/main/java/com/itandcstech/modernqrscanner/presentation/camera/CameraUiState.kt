package com.itandcstech.modernqrscanner.presentation.camera

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
data class CameraUiState(
    val isScanning: Boolean = true,
    val scannedContent: String? = null,
    val scannedType: String? = null,
    val error: String? = null
)

// States:
// isScanning = true  → Camera active, QR dhundh raha hai
// scannedContent != null → QR mil gaya, show result
// error != null → Camera permission nahi, ya koi issue