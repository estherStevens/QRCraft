package com.stevens.software.generator.data

import android.graphics.Bitmap

interface QrGeneratorRepository {
    fun createQrCode(qrData: String): Bitmap
}