package com.stevens.software.analyzer.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.time.OffsetDateTime

object BitmapUtils {

    fun saveBitmapToCache(context: Context, bitmap: Bitmap?): String {
        val file = File(context.cacheDir, OffsetDateTime.now().toString())
        file.outputStream().use { outputStream ->
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        return file.path
    }
}