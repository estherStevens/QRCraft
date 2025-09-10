package com.stevens.software.qrcraft.generate_qr.data_entry

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import androidx.core.graphics.set
import com.google.zxing.common.BitMatrix

class QrGeneratorRepository {

    fun createQrCode(qrData: String) : Bitmap {
        val bitMatrix = MultiFormatWriter().encode(
            qrData,
            BarcodeFormat.QR_CODE,
            200,
            200
        )
        return bitMatrix.toBitmap()
    }

    private fun BitMatrix.toBitmap() : Bitmap{
        val width = this.width
        val height = this.height

        val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

        for(x in 0 until width){
            for(y in 0 until height){
                bitmap[x, y] = if (this[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            }
        }
        return bitmap
    }
}