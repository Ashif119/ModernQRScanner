package com.itandcstech.modernqrscanner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
@Dao
interface QRResultDao {

    // Flow = live data stream
    // Jab bhi DB update hoga, automatically naya data milega
    @Query("SELECT * FROM qr_results ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<QRResultEntity>>

    // OnConflictStrategy.REPLACE = same ID aaye toh replace karo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQRResult(entity: QRResultEntity)

    @Query("DELETE FROM qr_results WHERE id = :id")
    suspend fun deleteQRResult(id: Int)
}