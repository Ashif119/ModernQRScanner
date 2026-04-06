package com.itandcstech.modernqrscanner.domain.repository

import com.itandcstech.modernqrscanner.domain.model.QRResult
import kotlinx.coroutines.flow.Flow

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
interface QRRepository {
    fun getAllScans(): Flow<List<QRResult>>
    suspend fun saveQRResult(result: QRResult)
    suspend fun deleteQRResult(id: Int)
}