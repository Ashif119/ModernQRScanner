package com.itandcstech.modernqrscanner.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itandcstech.modernqrscanner.domain.model.QRResult
import com.itandcstech.modernqrscanner.domain.usecase.DeleteQRResultUseCase
import com.itandcstech.modernqrscanner.domain.usecase.GetAllScansUseCase
import com.itandcstech.modernqrscanner.domain.usecase.SaveQRResultUseCase
import dagger.hilt.android.internal.lifecycle.HiltViewModelMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllScansUseCase: GetAllScansUseCase,
    private val saveQRResultUseCase: SaveQRResultUseCase,
    private val deleteQRResultUseCase: DeleteQRResultUseCase
) : ViewModel() {
    // Private = sirf ViewModel change kar sakta hai
    private val _uiState = MutableStateFlow(HomeUiState())

    // Public = UI sirf read kar sakti hai
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // ViewModel bante hi scan history load karo
        loadScanHistory()
    }

    private fun loadScanHistory() {
        getAllScansUseCase()
            .onEach { result ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        scanHistory = result,
                        error = null
                    )
                }
            }
            .catch {  exception ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = exception.message ?: "Something went wrong"
                    )
                }
            }
            .launchIn(viewModelScope)
    }
    fun saveQRResult(content: String, type: String) {
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

    fun deleteQRResult(id: Int) {
        viewModelScope.launch {
            try {
                deleteQRResultUseCase(id)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

// States:
// isScanning = true  → Camera active, QR dhundh raha hai
// scannedContent != null → QR mil gaya, show result
// error != null → Camera permission nahi, ya koi issue