package com.stevens.software.qrcraft.qr_camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.max

class BitmapAnalyzer(val context: Context){

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )
    fun analyzeFromBitmap(bitmap: Bitmap, onResult: (Pair<String, QrCodeData?>) -> Unit) {
        scanner.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { qrCodes ->
                var localBitmapPath = ""
                var localQrCodeData: QrCodeData? = null
                if (qrCodes.isNotEmpty()) {
                    qrCodes.forEach {
                        val rawValue = it.rawValue
                        if (rawValue.isNullOrBlank().not()) {
                            localBitmapPath = saveBitmapToCache(context, bitmap)
                            localQrCodeData = it.mapToQrCodeData()
                            // Assuming you only want the first valid one for simplicity
                            onResult(localBitmapPath to localQrCodeData)
                            return@addOnSuccessListener // Exit after finding the first one
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                onResult("" to null)
            }
    }


    private fun saveBitmapToCache(context: Context, bitmap: Bitmap?): String {
        val file = File(context.cacheDir, OffsetDateTime.now().toString())
        file.outputStream().use { outputStream ->
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        return file.path
    }
}

class QrCodeAnalyzer(
    private val context: Context,
    private val onQrCodeScanned: (String, QrCodeData?) -> Unit,
    private val onQrCodeDetected: () -> Unit,
): ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            var bitmapPath = ""
            var qrCodeData: QrCodeData? = null
            var qrScannedSuccessfully = false
            scanner.process(inputImage)
                .addOnSuccessListener { qrCode ->
                    if (qrScannedSuccessfully) return@addOnSuccessListener
                    qrCode.forEach {
                        onQrCodeDetected()
                        val rawValue = it.rawValue
                        if (rawValue.isNullOrBlank().not()) {
                            val bitmap = cropBitmap(imageProxy.toRotatedBitmap(), it)
                            bitmapPath = saveBitmapToCache(context, bitmap)
                            qrCodeData = it.mapToQrCodeData()

                            qrScannedSuccessfully = true
                        }
                    }
                }
                .addOnFailureListener {}
                .addOnCompleteListener {
                    imageProxy.close()
                    if (qrScannedSuccessfully) {
                        onQrCodeScanned(bitmapPath, qrCodeData)
                    }
                }
        } else {
            imageProxy.close()
        }
    }


    private fun cropBitmap(qrCodeBitmap: Bitmap, barcode: Barcode): Bitmap? {
        val box = barcode.boundingBox ?: return null
        val x = box.left.coerceAtLeast(0)
        val y = box.top.coerceAtLeast(0)
        val width = box.width().coerceAtMost(qrCodeBitmap.width - x)
        val height = box.height().coerceAtMost(qrCodeBitmap.height - y)
        val croppedBitmap = Bitmap.createBitmap(qrCodeBitmap, x, y, width, height)

        val scale = 1024f / max(croppedBitmap.width, croppedBitmap.height)
        val scaledBitmap = croppedBitmap.scale(
            (croppedBitmap.width * scale).toInt(),
            (croppedBitmap.height * scale).toInt()
        )
        return scaledBitmap

    }

    private fun saveBitmapToCache(context: Context, bitmap: Bitmap?): String {
        val file = File(context.cacheDir, OffsetDateTime.now().toString())
        file.outputStream().use { outputStream ->
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        return file.path
    }

    fun ImageProxy.toRotatedBitmap(): Bitmap {
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
        val yuv = out.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(yuv, 0, yuv.size)

        val matrix = Matrix()
        matrix.postRotate(imageInfo.rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

