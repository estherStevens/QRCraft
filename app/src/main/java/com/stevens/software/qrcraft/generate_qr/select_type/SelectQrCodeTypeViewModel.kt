package com.stevens.software.qrcraft.generate_qr.select_type

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.stevens.software.qrcraft.R

class SelectQrCodeTypeViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<SelectQrCodeTypeUiState> = MutableStateFlow(
        SelectQrCodeTypeUiState(qrCodeTypes())
    )
    val uiState = _uiState.asStateFlow()

    private fun qrCodeTypes(): List<QrCodeType> = buildList {
        add(QrCodeType(qrType = QrType.TEXT, drawable = R.drawable.text_qr_type_icon, text = R.string.qr_type_text))
        add(QrCodeType(qrType = QrType.LINK, drawable = R.drawable.link_qr_type_icon, text = R.string.qr_type_link))
        add(QrCodeType(qrType = QrType.CONTACT, drawable = R.drawable.contact_details_qr_type_icon, text = R.string.qr_type_contact_details))
        add(QrCodeType(qrType = QrType.PHONE_NUMBER, drawable = R.drawable.phone_number_qr_type_icon, text = R.string.qr_type_phone_number))
        add(QrCodeType(qrType = QrType.GEOLOCATION, drawable = R.drawable.geolocation_qr_type_icon, text = R.string.qr_type_geolocation))
        add(QrCodeType(qrType = QrType.WIFI, drawable = R.drawable.wifi_qr_type_icon, text = R.string.qr_type_wifi))
    }
}

data class QrCodeType(val qrType: QrType, val drawable: Int, val text: Int)
enum class QrType{
    TEXT,
    LINK,
    CONTACT,
    PHONE_NUMBER,
    GEOLOCATION,
    WIFI
}

data class SelectQrCodeTypeUiState(
    val qrCodeTypes: List<QrCodeType>
)