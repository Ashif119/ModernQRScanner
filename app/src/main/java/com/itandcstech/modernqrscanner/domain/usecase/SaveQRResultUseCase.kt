package com.itandcstech.modernqrscanner.domain.usecase

import com.itandcstech.modernqrscanner.domain.model.QRResult
import com.itandcstech.modernqrscanner.domain.repository.QRRepository
import javax.inject.Inject

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
class SaveQRResultUseCase @Inject constructor(private val repository: QRRepository) {
    suspend operator fun invoke(result: QRResult) =
        repository.saveQRResult(result)
}