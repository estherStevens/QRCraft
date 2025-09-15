package com.stevens.software.scanner.data

import com.google.mlkit.vision.barcode.common.Barcode

//fun Barcode.mapToQrCodeData() =
//    when(this.valueType) {
//        Barcode.TYPE_WIFI -> {
//            QrCodeData.Wifi(
//                ssid = this.wifi?.ssid .orEmpty(),
//                password = this.wifi?.password.orEmpty(),
//                encryptionType = this.wifi?.encryptionType.toString()
//            )
//        }
//        Barcode.TYPE_PHONE -> {
//            QrCodeData.PhoneNumber(
//                phoneNumber = this.phone?.number.orEmpty()
//            )
//        }
//        Barcode.TYPE_GEO -> {
//            QrCodeData.Geolocation(
//                latitude = this.geoPoint?.lat.toString(),
//                longitude = this.geoPoint?.lng.toString()
//            )
//        }
//        Barcode.TYPE_URL -> {
//            QrCodeData.Url(
//                link = this.url?.url.orEmpty()
//            )
//        }
//        Barcode.TYPE_CONTACT_INFO -> {
//            QrCodeData.ContactDetails(
//                name = this.contactInfo?.name?.formattedName.orEmpty(),
//                tel = this.contactInfo?.phones?.first()?.number.orEmpty(),
//                email = this.contactInfo?.emails?.first()?.address.orEmpty()
//            )
//        }
//        Barcode.TYPE_TEXT -> {
//            QrCodeData.PlainText(
//                text = this.displayValue.orEmpty()
//            )
//        }
//        else -> null
//    }

