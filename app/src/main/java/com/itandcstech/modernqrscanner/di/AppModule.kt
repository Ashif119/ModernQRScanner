package com.itandcstech.modernqrscanner.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.itandcstech.modernqrscanner.data.local.QRResultDao
import com.itandcstech.modernqrscanner.data.local.QRScannerDatabase
import com.itandcstech.modernqrscanner.data.repository.QRRepositoryImpl
import com.itandcstech.modernqrscanner.domain.repository.QRRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Created by Ashif on 06-04-2026
 * Know more about author at https://ashif.nexmerce.in
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Step 1: Database banao
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QRScannerDatabase {
        return Room.databaseBuilder(
            context,
            QRScannerDatabase::class.java,
            "qr_scanner_db"    // DB file ka naam
        ).build()
    }

    // Step 2: DAO banao (Database se milta hai)
    @Provides
    @Singleton
    fun provideQrResultDao(
        database: QRScannerDatabase
    ): QRResultDao{
        return database.qrResultDao()
    }

    // Step 3: Repository banao
    // Interface → Implementation mapping yahan hoti hai!
    @Provides
    @Singleton
    fun provideQRRepository(
        dao: QRResultDao
    ): QRRepository {
        return QRRepositoryImpl(dao)
    }
}