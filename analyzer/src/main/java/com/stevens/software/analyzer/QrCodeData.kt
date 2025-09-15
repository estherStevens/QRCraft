package com.stevens.software.analyzer


sealed class QrCodeData() {
    open val qrBitmapPath: String = ""

    data class ContactDetails(override val qrBitmapPath: String, val name: String, val tel: String, val email: String) : QrCodeData()
    data class Url(override val qrBitmapPath: String, val link: String) : QrCodeData()
    data class Geolocation(override val qrBitmapPath: String, val latitude: String, val longitude: String) : QrCodeData()
    data class PhoneNumber(override val qrBitmapPath: String, val phoneNumber: String) : QrCodeData()
    data class Wifi(override val qrBitmapPath: String, val ssid: String, val password: String, val encryptionType: String) : QrCodeData()
    data class PlainText(override val qrBitmapPath: String, val text: String) : QrCodeData()
}
