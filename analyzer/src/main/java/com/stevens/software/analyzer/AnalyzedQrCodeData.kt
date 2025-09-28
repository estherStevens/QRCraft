package com.stevens.software.analyzer

sealed class AnalyzedQrCodeData() {
    open val qrBitmapPath: String = ""

    data class ContactDetails(override val qrBitmapPath: String, val name: String, val tel: String, val email: String) : AnalyzedQrCodeData()
    data class Url(override val qrBitmapPath: String, val link: String) : AnalyzedQrCodeData()
    data class Geolocation(override val qrBitmapPath: String, val latitude: String, val longitude: String) : AnalyzedQrCodeData()
    data class PhoneNumber(override val qrBitmapPath: String, val phoneNumber: String) : AnalyzedQrCodeData()
    data class Wifi(override val qrBitmapPath: String, val ssid: String, val password: String, val encryptionType: String) : AnalyzedQrCodeData()
    data class PlainText(override val qrBitmapPath: String, val text: String) : AnalyzedQrCodeData()
}
