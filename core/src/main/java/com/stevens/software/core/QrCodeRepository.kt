package com.stevens.software.core

import android.content.Context
import kotlinx.coroutines.flow.Flow

class QrCodeRepository(private val context: Context) {
    private val qrCodeDao = QrCodeDatabase.getDatabase(context).qrCodeDap()

    fun getAllQrCodes(): Flow<List<QrCode>> = qrCodeDao.getAllQrCodes()

    fun getQrCode(id: Long): Flow<QrCode?> = qrCodeDao.getQrCode(id)

    suspend fun insertQrCode(qrCode: QrCode): Long = qrCodeDao.insert(qrCode)

    suspend fun deleteQrCode(qrCode: QrCode) = qrCodeDao.delete(qrCode)

    suspend fun updateQrCode(qrCode: QrCode) = qrCodeDao.update(qrCode)
}