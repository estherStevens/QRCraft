package com.stevens.software.generator.data

import android.graphics.Bitmap
import com.stevens.software.generator.ui.QrData

interface QrGeneratorRepository {
    fun createQrCode(qrData: QrData): Bitmap
}