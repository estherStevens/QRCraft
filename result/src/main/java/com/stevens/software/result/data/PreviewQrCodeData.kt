package com.stevens.software.result.data


sealed class PreviewQrCodeData() {
    open val qrBitmapPath: String = ""
    open val isFavourite: Boolean = false

    data class ContactDetails(override val isFavourite: Boolean, override val qrBitmapPath: String, val name: String, val tel: String, val email: String) : PreviewQrCodeData()
    data class Url(override val isFavourite: Boolean, override val qrBitmapPath: String, val link: String) : PreviewQrCodeData()
    data class Geolocation(override val isFavourite: Boolean, override val qrBitmapPath: String, val latitude: String, val longitude: String) : PreviewQrCodeData()
    data class PhoneNumber(override val isFavourite: Boolean, override val qrBitmapPath: String, val phoneNumber: String) : PreviewQrCodeData()
    data class Wifi(override val isFavourite: Boolean, override val qrBitmapPath: String, val ssid: String, val password: String, val encryptionType: String) : PreviewQrCodeData()
    data class PlainText(override val isFavourite: Boolean, override val qrBitmapPath: String, val text: String) : PreviewQrCodeData()
}
