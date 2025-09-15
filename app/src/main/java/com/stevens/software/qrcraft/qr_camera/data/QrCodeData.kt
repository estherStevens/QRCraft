package com.stevens.software.qrcraft.qr_camera.data


sealed interface QrCodeData {
    data class ContactDetails(val name: String, val tel: String, val email: String) : QrCodeData
    data class Url(val link: String) : QrCodeData
    data class Geolocation(val latitude: String, val longitude: String) : QrCodeData
    data class PhoneNumber(val phoneNumber: String) : QrCodeData
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : QrCodeData
    data class PlainText(val text: String) : QrCodeData
}


sealed interface NewQrCodeData {
    data class ContactDetails(val qrBitmapPath: String, val name: String, val tel: String, val email: String) : NewQrCodeData
    data class Url(val qrBitmapPath: String, val link: String) : NewQrCodeData
    data class Geolocation(val qrBitmapPath: String, val latitude: String, val longitude: String) : NewQrCodeData
    data class PhoneNumber(val qrBitmapPath: String, val phoneNumber: String) : NewQrCodeData
    data class Wifi(val qrBitmapPath: String, val ssid: String, val password: String, val encryptionType: String) : NewQrCodeData
    data class PlainText(val qrBitmapPath: String, val text: String) : NewQrCodeData
}
