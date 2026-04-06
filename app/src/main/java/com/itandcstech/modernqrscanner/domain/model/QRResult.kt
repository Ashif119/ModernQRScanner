package com.itandcstech.modernqrscanner.domain.model

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
data class QRResult(
    val id: Int = 0,
    val content: String,
    val type: String,
    val timestamp: Long
)