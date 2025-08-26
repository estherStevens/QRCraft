package com.stevens.software.qrcraft.qr_camera.data

import kotlinx.serialization.Serializable

@Serializable
sealed interface QrCodeData {
    @Serializable data class ContactDetails(val name: String, val tel: String, val email: String) : QrCodeData
    @Serializable data class Url(val link: String) : QrCodeData
    @Serializable data class Geolocation(val latitude: String, val longitude: String) : QrCodeData
    @Serializable data class PhoneNumber(val phoneNumber: String) : QrCodeData
    @Serializable data class Wifi(val ssid: String, val password: String, val encryptionType: String) : QrCodeData
    @Serializable data class PlainText(val text: String) : QrCodeData
}
