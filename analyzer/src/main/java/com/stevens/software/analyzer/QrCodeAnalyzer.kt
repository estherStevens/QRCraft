package com.stevens.software.result

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.analyzer.utils.BitmapUtils.saveBitmapToCache
import com.stevens.software.analyzer.utils.MlKitScannerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class QrCodeAnalyzer(val context: Context){

    private val scanner = BarcodeScanning.getClient(MlKitScannerOptions.QrCode)

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun extractDataFromQr(bitmap: Bitmap) : QrCodeData? {
        return suspendCancellableCoroutine { continuation ->
            scanner.process(InputImage.fromBitmap(bitmap, 0))
                .addOnSuccessListener { qrCodes ->
                    if (!continuation.isActive) return@addOnSuccessListener
                    if (qrCodes.isNotEmpty()) {
                        var qrCodeData: QrCodeData? = null
                        val localBitmapPath = saveBitmapToCache(context, bitmap)
                        qrCodes.forEach {
                            val rawValue = it.rawValue
                            if (rawValue.isNullOrBlank().not()) {
                                qrCodeData = it.mapToQrCodeData(localBitmapPath)
                            }
                        }
                        continuation.resume(qrCodeData)
                    }
                }
                .addOnFailureListener {
                    if (continuation.isActive) {
                        continuation.resume(null)
                    }
                }
        }
    }
}

fun Barcode.mapToQrCodeData(qrBitmapPath: String) =
    when(this.valueType) {
        Barcode.TYPE_WIFI -> {
            QrCodeData.Wifi(
                qrBitmapPath = qrBitmapPath,
                ssid = this.wifi?.ssid .orEmpty(),
                password = this.wifi?.password.orEmpty(),
                encryptionType = this.wifi?.encryptionType.toString()
            )
        }
        Barcode.TYPE_PHONE -> {
            QrCodeData.PhoneNumber(
                qrBitmapPath = qrBitmapPath,
                phoneNumber = this.phone?.number.orEmpty()
            )
        }
        Barcode.TYPE_GEO -> {
            QrCodeData.Geolocation(
                qrBitmapPath = qrBitmapPath,
                latitude = this.geoPoint?.lat.toString(),
                longitude = this.geoPoint?.lng.toString()
            )
        }
        Barcode.TYPE_URL -> {
            QrCodeData.Url(
                qrBitmapPath = qrBitmapPath,
                link = this.url?.url.orEmpty()
            )
        }
        Barcode.TYPE_CONTACT_INFO -> {
            QrCodeData.ContactDetails(
                qrBitmapPath = qrBitmapPath,
                name = this.contactInfo?.name?.formattedName.orEmpty(),
                tel = this.contactInfo?.phones?.first()?.number.orEmpty(),
                email = this.contactInfo?.emails?.first()?.address.orEmpty()
            )
        }
        Barcode.TYPE_TEXT -> {
            QrCodeData.PlainText(
                qrBitmapPath = qrBitmapPath,
                text = this.displayValue.orEmpty()
            )
        }
        else -> null
    }
