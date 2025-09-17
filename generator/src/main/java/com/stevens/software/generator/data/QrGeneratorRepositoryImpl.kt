package com.stevens.software.generator.data

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.stevens.software.generator.ui.QrData
import java.util.EnumMap
import kotlin.collections.set

class QrGeneratorRepositoryImpl: QrGeneratorRepository {

    override fun createQrCode(qrData: QrData): Bitmap {
        val qrCodeData = qrData.toQrCodeString()
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.MARGIN] = 1
        val bitMatrix = MultiFormatWriter().encode(
            qrCodeData,
            BarcodeFormat.QR_CODE,
            300,
            300,
            hints
        )
        return bitMatrix.toBitmap()
    }

    private fun QrData.toQrCodeString() : String =
        when(this) {
            is QrData.Contact -> """
                BEGIN:VCARD
                VERSION:3.0
                N:$name
                TEL:$phoneNumber
                EMAIL:$email
            """.trimIndent()
            is QrData.Geolocation -> "geo:$latitude,$longitude"
            is QrData.Link -> url
            is QrData.PhoneNumber -> "TEL:$phoneNumber"
            is QrData.Text -> text
            is QrData.Wifi -> "WIFI:S:$ssid;T:$encryptionType;P:$password;;"
        }

    private fun BitMatrix.toBitmap(): Bitmap {
        val width = this.width
        val height = this.height
        val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap[x, y] = if (this[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            }
        }
        return bitmap
    }
}