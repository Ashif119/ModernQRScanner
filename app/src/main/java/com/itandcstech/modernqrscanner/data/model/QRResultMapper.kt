package com.itandcstech.modernqrscanner.data.model

import com.itandcstech.modernqrscanner.data.local.QRResultEntity
import com.itandcstech.modernqrscanner.domain.model.QRResult

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
// Extension functions — Kotlin ka superpower!
// Entity ko Domain mein convert karo
fun QRResultEntity.toDomain(): QRResult {
    return QRResult(
        id = id,
        content = content,
        type = type,
        timestamp = timestamp
    )
}

// Domain ko Entity mein convert karo
fun QRResult.toEntity(): QRResultEntity {
    return QRResultEntity(
        id = id,
        content = content,
        type = type,
        timestamp = timestamp
    )
}