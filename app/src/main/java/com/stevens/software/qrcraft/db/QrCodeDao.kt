package com.stevens.software.qrcraft.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QrCodeDao{
    @Insert
    suspend fun insert(qrCode: QrCode)

    @Delete
    suspend fun delete(qrCode: QrCode)

    @Update
    suspend fun update(qrCode: QrCode)

    @Query("Select * from qrCodes where id = :id")
    fun getQrCode(id: Int) : Flow<QrCode>

    @Query("Select * from qrCodes")
    fun getAllQrCodes() : Flow<List<QrCode>>
}