package com.stevens.software.qrcraft.qr_camera.data

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

fun Barcode.mapToQrCodeDataNew(qrBitmapPath: String) =
    when(this.valueType) {
        Barcode.TYPE_WIFI -> {
            NewQrCodeData.Wifi(
                qrBitmapPath = qrBitmapPath,
                ssid = this.wifi?.ssid .orEmpty(),
                password = this.wifi?.password.orEmpty(),
                encryptionType = this.wifi?.encryptionType.toString()
            )
        }
        Barcode.TYPE_PHONE -> {
            NewQrCodeData.PhoneNumber(
                qrBitmapPath = qrBitmapPath,
                phoneNumber = this.phone?.number.orEmpty()
            )
        }
        Barcode.TYPE_GEO -> {
            NewQrCodeData.Geolocation(
                qrBitmapPath = qrBitmapPath,
                latitude = this.geoPoint?.lat.toString(),
                longitude = this.geoPoint?.lng.toString()
            )
        }
        Barcode.TYPE_URL -> {
            NewQrCodeData.Url(
                qrBitmapPath = qrBitmapPath,
                link = this.url?.url.orEmpty()
            )
        }
        Barcode.TYPE_CONTACT_INFO -> {
            NewQrCodeData.ContactDetails(
                qrBitmapPath = qrBitmapPath,
                name = this.contactInfo?.name?.formattedName.orEmpty(),
                tel = this.contactInfo?.phones?.first()?.number.orEmpty(),
                email = this.contactInfo?.emails?.first()?.address.orEmpty()
            )
        }
        Barcode.TYPE_TEXT -> {
            NewQrCodeData.PlainText(
                qrBitmapPath = qrBitmapPath,
                text = this.displayValue.orEmpty()
            )
        }
        else -> null
    }
