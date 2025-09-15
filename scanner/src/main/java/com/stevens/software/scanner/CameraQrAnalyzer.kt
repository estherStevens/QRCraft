package com.stevens.software.scanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.graphics.scale
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.stevens.software.analyzer.utils.MlKitScannerOptions
import java.io.ByteArrayOutputStream
import kotlin.math.max

class CameraQrAnalyzer(
    private val onQrCodeScanned: (Bitmap?) -> Unit,
    private val onQrCodeDetected: () -> Unit,
): ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient(MlKitScannerOptions.QrCode)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            var bitmap: Bitmap? = null
            var qrScannedSuccessfully = false
            scanner.process(inputImage)
                .addOnSuccessListener { qrCode ->
                    if (qrScannedSuccessfully) return@addOnSuccessListener
                    qrCode.forEach {
                        onQrCodeDetected()
                        val rawValue = it.rawValue
                        if (rawValue.isNullOrBlank().not()) {
                            bitmap = cropBitmap(imageProxy.toRotatedBitmap(), it)
                            qrScannedSuccessfully = true
                        }
                    }
                }
                .addOnFailureListener {}
                .addOnCompleteListener {
                    imageProxy.close()
                    if (qrScannedSuccessfully) {
                        onQrCodeScanned(bitmap)
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

    private fun ImageProxy.toRotatedBitmap(): Bitmap {
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