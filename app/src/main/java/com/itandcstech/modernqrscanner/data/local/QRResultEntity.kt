package com.itandcstech.modernqrscanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
@Entity(tableName = "qr_results")
data class QRResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val type: String,
    val timestamp: Long
)