package com.itandcstech.modernqrscanner.data.repository

import com.itandcstech.modernqrscanner.data.local.QRResultDao
import com.itandcstech.modernqrscanner.data.model.toDomain
import com.itandcstech.modernqrscanner.data.model.toEntity
import com.itandcstech.modernqrscanner.domain.model.QRResult
import com.itandcstech.modernqrscanner.domain.repository.QRRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
class QRRepositoryImpl @Inject constructor(
    private val dao: QRResultDao  // Hilt inject karega automatically
) : QRRepository {

    override fun getAllScans(): Flow<List<QRResult>> {
        return dao.getAllScans().map { entities ->
            entities.map { it.toDomain() }  // Entity → Domain convert
        }
    }

    override suspend fun saveQRResult(result: QRResult) {
        dao.insertQRResult(result.toEntity())  // Domain → Entity convert
    }

    override suspend fun deleteQRResult(id: Int) {
        dao.deleteQRResult(id)
    }
}