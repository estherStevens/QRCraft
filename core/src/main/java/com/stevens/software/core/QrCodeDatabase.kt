package com.stevens.software.qrcraft.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stevens.software.core.QrResultConverter

@Database(entities = [QrCode::class], version = 1, exportSchema = false)
@TypeConverters(QrResultConverter::class)
abstract class QrCodeDatabase: RoomDatabase(){

    abstract fun qrCodeDap(): QrCodeDao

    companion object {
        @Volatile
        private var Instance: QrCodeDatabase? = null

        fun getDatabase(context: Context): QrCodeDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, QrCodeDatabase::class.java, "qrcode_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}