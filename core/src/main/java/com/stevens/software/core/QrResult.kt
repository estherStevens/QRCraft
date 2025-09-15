package com.stevens.software.core

import kotlinx.serialization.Serializable

@Serializable
sealed class QrResult {
    @Serializable
    data class PhoneNumber(val phoneNumber: String) : QrResult()

    @Serializable
    data class Link(val url: String) : QrResult()

    @Serializable
    data class Wifi(val ssid: String, val password: String, val encryptionType: String) : QrResult()

    @Serializable
    data class Geolocation(val latitude: String, val longitude: String) : QrResult()

    @Serializable
    data class Contact(val name: String, val phone: String, val email: String) : QrResult()

    @Serializable
    data class PlainText(val text: String) : QrResult()
}