package com.stevens.software.core

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QrCodeDao{
    @Insert
    suspend fun insert(qrCode: QrCode) : Long

    @Query("DELETE FROM qrCodes WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Update
    suspend fun update(qrCode: QrCode)

    @Query("Select * from qrCodes where id = :id")
    fun getQrCode(id: Long) : Flow<QrCode>

    @Query("Select * from qrCodes")
    fun getAllQrCodes() : Flow<List<QrCode>>

    @Query("UPDATE qrCodes SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updateFavouriteStatus(id: Int, isFavourite: Boolean)
}