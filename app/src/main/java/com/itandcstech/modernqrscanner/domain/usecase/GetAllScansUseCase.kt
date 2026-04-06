package com.itandcstech.modernqrscanner.domain.usecase

import com.itandcstech.modernqrscanner.domain.repository.QRRepository
import javax.inject.Inject
/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
class GetAllScansUseCase @Inject constructor(
    private val repository: QRRepository
) {
    operator fun invoke() = repository.getAllScans()
}