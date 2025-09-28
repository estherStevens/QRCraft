package com.stevens.software.result.data

sealed class QrCodeData() {
    open val qrBitmapPath: String = ""
    open val isFavourite: Boolean = false
    open val id: Int = -1

    data class ContactDetails(override val isFavourite: Boolean, override val qrBitmapPath: String, val name: String, val tel: String, val email: String) : QrCodeData()
    data class Url(override val isFavourite: Boolean, override val qrBitmapPath: String, val link: String) : QrCodeData()
    data class Geolocation(override val isFavourite: Boolean, override val qrBitmapPath: String, val latitude: String, val longitude: String) : QrCodeData()
    data class PhoneNumber(override val isFavourite: Boolean, override val qrBitmapPath: String, val phoneNumber: String) : QrCodeData()
    data class Wifi(override val isFavourite: Boolean, override val qrBitmapPath: String, val ssid: String, val password: String, val encryptionType: String) : QrCodeData()
    data class PlainText(override val isFavourite: Boolean, override val qrBitmapPath: String, val text: String) : QrCodeData()
}
