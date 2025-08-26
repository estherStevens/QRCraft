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

    private fun qrCodeTypes(): List<QrType> = buildList {
        add(QrType(drawable = R.drawable.text_qr_type_icon, text = R.string.qr_type_text))
        add(QrType(drawable = R.drawable.link_qr_type_icon, text = R.string.qr_type_link))
        add(QrType(drawable = R.drawable.contact_details_qr_type_icon, text = R.string.qr_type_contact_details))
        add(QrType(drawable = R.drawable.phone_number_qr_type_icon, text = R.string.qr_type_phone_number))
        add(QrType(drawable = R.drawable.geolocation_qr_type_icon, text = R.string.qr_type_geolocation))
        add(QrType(drawable = R.drawable.wifi_qr_type_icon, text = R.string.qr_type_wifi))
    }
}

data class QrType(val drawable: Int, val text: Int)

data class SelectQrCodeTypeUiState(
    val qrTypes: List<QrType>
)