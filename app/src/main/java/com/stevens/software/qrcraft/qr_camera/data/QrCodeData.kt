package com.stevens.software.qrcraft.qr_camera.data


sealed interface QrCodeData {
    data class ContactDetails(val name: String, val tel: String, val email: String) : QrCodeData
    data class Url(val link: String) : QrCodeData
    data class Geolocation(val latitude: String, val longitude: String) : QrCodeData
    data class PhoneNumber(val phoneNumber: String) : QrCodeData
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : QrCodeData
    data class PlainText(val text: String) : QrCodeData
}
