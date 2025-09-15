package com.stevens.software.qrcraft.db

@kotlinx.serialization.Serializable
sealed class QrResult {
    @kotlinx.serialization.Serializable
    data class PhoneNumber(val phoneNumber: String) : QrResult()

    @kotlinx.serialization.Serializable
    data class Link(val url: String) : QrResult()

    @kotlinx.serialization.Serializable
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : QrResult()

    @kotlinx.serialization.Serializable
    data class Geolocation(val latitude: String, val longitude: String) : QrResult()

    @kotlinx.serialization.Serializable
    data class Contact(val name: String, val phone: String, val email: String) : QrResult()

    @kotlinx.serialization.Serializable
    data class PlainText(val text: String) : QrResult()
}