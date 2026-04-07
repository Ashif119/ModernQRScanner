package com.itandcstech.modernqrscanner.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itandcstech.modernqrscanner.domain.model.QRResult
import com.itandcstech.modernqrscanner.domain.usecase.SaveQRResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val saveQRResultUseCase: SaveQRResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()
    fun onQRCodeDetected(content: String) {
        // Ek baar scan hone ke baad dobara scan mat karo
        if (_uiState.value.scannedContent != null) return

        val type = detectQRType(content)

        _uiState.update {
            it.copy(
                isScanning = false,
                scannedContent = content,
                scannedType = type
            )
        }

        // Automatically save karo DB mein
        saveScannedResult(content, type)
    }

    private fun saveScannedResult(content: String, type: String) {
        viewModelScope.launch {
            try {
                saveQRResultUseCase(
                    QRResult(
                        content = content,
                        type = type,
                        timestamp = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun resetScanner() {
        _uiState.update {
            it.copy(
                isScanning = true,
                scannedContent = null,
                scannedType = null,
                error = null
            )
        }
    }

    // QR content ka type detect karo
    private fun detectQRType(content: String): String {
        return when {
            content.startsWith("http") -> "URL"
            content.startsWith("mailto") -> "EMAIL"
            content.startsWith("tel") -> "PHONE"
            content.startsWith("WIFI") -> "WIFI"
            else -> "TEXT"
        }
    }

}