package com.stevens.software.generator.data

sealed class CustomQrCodeData() {
    open val qrBitmapPath: String = ""

    data class ContactDetails(override val qrBitmapPath: String, val name: String, val tel: String, val email: String) : CustomQrCodeData()
    data class Url(override val qrBitmapPath: String, val link: String) : CustomQrCodeData()
    data class Geolocation(override val qrBitmapPath: String, val latitude: String, val longitude: String) : CustomQrCodeData()
    data class PhoneNumber(override val qrBitmapPath: String, val phoneNumber: String) : CustomQrCodeData()
    data class Wifi(override val qrBitmapPath: String, val ssid: String, val password: String, val encryptionType: String) : CustomQrCodeData()
    data class PlainText(override val qrBitmapPath: String, val text: String) : CustomQrCodeData()
}