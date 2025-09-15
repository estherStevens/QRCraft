package com.stevens.software.core

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class QrResultConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromQrResult(qrResult: QrResult?): String? {
        return qrResult?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toQrResult(data: String?): QrResult? {
        return data?.let { json.decodeFromString<QrResult>(it) }
    }
}