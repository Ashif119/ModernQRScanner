package com.itandcstech.modernqrscanner.presentation.home

import com.itandcstech.modernqrscanner.domain.model.QRResult

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val scanHistory: List<QRResult> = emptyList(),
    val error: String? = null
)

// Yeh 3 scenarios cover karta hai:
// 1. isLoading = true  → Show loading spinner
// 2. scanHistory filled → Show list
// 3. error != null     → Show error message