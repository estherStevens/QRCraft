package com.stevens.software.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.core.QrCode
import com.stevens.software.core.QrCodeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QrHistoryViewModel(
    private val qrCodeRepository: QrCodeRepository
): ViewModel() {

    val uiState: StateFlow<QrHistoryUiState> = qrCodeRepository.getAllQrCodes().map {
        val o = it
        QrHistoryUiState(listOf())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        QrHistoryUiState(qrs = listOf())
    )

}

data class QrHistoryUiState(
    val qrs: List<HistoricQrCode>
)

sealed interface HistoricQrCode{
    data class ContactDetails(val qrType: String, val name: String, val tel: String, val email: String, val createdAt: String) : HistoricQrCode
    data class Url(val qrType: String, val link: String, val createdAt: String) : HistoricQrCode
    data class Geolocation(val qrType: String, val latitude: String, val longitude: String, val createdAt: String) : HistoricQrCode
    data class PhoneNumber(val qrType: String, val phoneNumber: String, val createdAt: String) : HistoricQrCode
    data class Wifi(val qrType: String, val ssid: String, val password: String, val encryptionType: String, val createdAt: String) : HistoricQrCode
    data class PlainText(val qrType: String, val text: String, val createdAt: String) : HistoricQrCode
}