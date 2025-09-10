package com.stevens.software.qrcraft.scanned_qr_result.data

import android.graphics.Bitmap
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import kotlinx.coroutines.flow.StateFlow

interface QrDataParser {
    val qrData: StateFlow<QrCodeData?>
    fun parseQrData(qrRawData: String)
}