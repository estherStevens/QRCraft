package com.stevens.software.qrcraft.qr_camera

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.stevens.software.qrcraft.qr_camera.data.NewQrCodeData
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
//import com.stevens.software.qrcraft.qr_camera.data.mapToQrCodeData
import com.stevens.software.qrcraft.qr_camera.data.mapToQrCodeDataNew
import com.stevens.software.qrcraft.utils.BitmapUtils.saveBitmapToCache
import com.stevens.software.qrcraft.utils.MlKitScannerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class QrCodeAnalyzer(val context: Context){

    private val scanner = BarcodeScanning.getClient(MlKitScannerOptions.QrCode)

//    fun saveQrBitmapToFile(bitmap: Bitmap, onResult: (String) -> Unit) {
//        scanner.process(InputImage.fromBitmap(bitmap, 0))
//            .addOnSuccessListener { qrCodes ->
//                var localBitmapPath = ""
//                if (qrCodes.isNotEmpty()) {
//                    qrCodes.forEach {
//                        val rawValue = it.rawValue
//                        if (rawValue.isNullOrBlank().not()) {
//                            localBitmapPath = saveBitmapToCache(context, bitmap)
//                            onResult(localBitmapPath)
//                            return@addOnSuccessListener
//                        }
//                    }
//                }
//            }
//    }

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    suspend fun extractDataFromQr(bitmap: Bitmap) : NewQrCodeData? {
        return suspendCancellableCoroutine { continuation ->
            scanner.process(InputImage.fromBitmap(bitmap, 0))
                .addOnSuccessListener { qrCodes ->
                    if (!continuation.isActive) return@addOnSuccessListener
                    if (qrCodes.isNotEmpty()) {
                        var qrCodeData: NewQrCodeData? = null
                        val localBitmapPath = saveBitmapToCache(context, bitmap)
                        qrCodes.forEach {
                            val rawValue = it.rawValue
                            if (rawValue.isNullOrBlank().not()) {
                                qrCodeData = it.mapToQrCodeDataNew(localBitmapPath)
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



