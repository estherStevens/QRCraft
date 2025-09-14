package com.stevens.software.qrcraft.qr_camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import android.graphics.Matrix
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.OffsetDateTime
import androidx.core.graphics.scale
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.qr_camera.data.mapToQrCodeData
import com.stevens.software.qrcraft.utils.BitmapUtils.saveBitmapToCache
import com.stevens.software.qrcraft.utils.MlKitScannerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.max

class BitmapAnalyzer(val context: Context){

    private val scanner = BarcodeScanning.getClient(MlKitScannerOptions.QrCode)

    fun saveQrBitmapToFile(bitmap: Bitmap, onResult: (String) -> Unit) {
        scanner.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { qrCodes ->
                var localBitmapPath = ""
                if (qrCodes.isNotEmpty()) {
                    qrCodes.forEach {
                        val rawValue = it.rawValue
                        if (rawValue.isNullOrBlank().not()) {
                            localBitmapPath = saveBitmapToCache(context, bitmap)
                            onResult(localBitmapPath)
                            return@addOnSuccessListener
                        }
                    }
                }
            }
    }

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    suspend fun extractDataFromQr(bitmap: Bitmap) : QrCodeData? {
        return suspendCancellableCoroutine { continuation ->
            scanner.process(InputImage.fromBitmap(bitmap, 0))
                .addOnSuccessListener { qrCodes ->
                    if (!continuation.isActive) return@addOnSuccessListener
                    if (qrCodes.isNotEmpty()) {
                        var qrCodeData: QrCodeData? = null
                        qrCodes.forEach {
                            val rawValue = it.rawValue
                            if (rawValue.isNullOrBlank().not()) {
                                qrCodeData = it.mapToQrCodeData()
                            }
                        }
                        continuation.resume(qrCodeData)
                    }
                }
                .addOnFailureListener {
                    if(continuation.isActive) {
                        continuation.resume(null)
                    }
                }
        }
    }
}



