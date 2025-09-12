package com.stevens.software.qrcraft.generate_qr.data_entry.data

import android.graphics.Bitmap

interface QrGeneratorRepository {
    fun createQrCode(qrData: String): Bitmap
}