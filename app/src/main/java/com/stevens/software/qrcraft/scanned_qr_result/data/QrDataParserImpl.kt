package com.stevens.software.qrcraft.scanned_qr_result.data

import android.graphics.Bitmap
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class QrDataParserImpl: QrDataParser {

    private val _qrData: MutableStateFlow<QrCodeData?> = MutableStateFlow(null)
    override val qrData = _qrData.asStateFlow()

    override fun parseQrData(qrRawData: String) {
        val qrData: QrCodeData = format.decodeFromString(qrRawData)
        _qrData.update { qrData }
    }
}


val format = Json {
    serializersModule = SerializersModule {
        polymorphic(QrCodeData::class) {
            subclass(QrCodeData.ContactDetails::class, QrCodeData.ContactDetails.serializer())
            subclass(QrCodeData.Url::class, QrCodeData.Url.serializer())
            subclass(QrCodeData.Geolocation::class, QrCodeData.Geolocation.serializer())
            subclass(QrCodeData.PhoneNumber::class, QrCodeData.PhoneNumber.serializer())
            subclass(QrCodeData.Wifi::class, QrCodeData.Wifi.serializer())
            subclass(QrCodeData.PlainText::class, QrCodeData.PlainText.serializer())
        }
    }
}
