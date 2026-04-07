package com.itandcstech.modernqrscanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
@Database(
    entities = [QRResultEntity::class],  // Saari tables yahan list karo
    version = 1,                          // Schema change hone pe badhao
    exportSchema = false                  // Schema file export mat karo
)
abstract class QRScannerDatabase : RoomDatabase() {
    abstract fun qrResultDao(): QRResultDao
}