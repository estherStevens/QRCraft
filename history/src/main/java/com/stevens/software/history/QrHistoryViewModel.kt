package com.stevens.software.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.core.QrCode
import com.stevens.software.core.QrCodeRepository
import com.stevens.software.core.QrResult
import com.stevens.software.history.HistoricQrCode.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class QrHistoryViewModel(
    private val qrCodeRepository: QrCodeRepository
): ViewModel() {

    val uiState: StateFlow<QrHistoryUiState> = qrCodeRepository.getAllQrCodes().map { qrs ->
        val allQrs: List<HistoricQrCode> = qrs.mapNotNull { it.toHistoricQrCode() }

        QrHistoryUiState(
            scannedQrs = allQrs.filterNot { it.userGenerated },
            generatedQrs = allQrs.filter { it.userGenerated }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        QrHistoryUiState(scannedQrs = listOf(), generatedQrs = listOf())
    )

    fun deleteQrCode(id: Int) {
        viewModelScope.launch {
            qrCodeRepository.deleteQrCode(id)
        }
    }

    private fun QrCode.toHistoricQrCode() : HistoricQrCode? {
        val id = this.id
        val parsedData = this.parsedData
        val createdAt = this.dateCreated
        return when(parsedData){
            is QrResult.Contact -> {
                ContactDetails(
                    id = id,
                    name = parsedData.name,
                    email = parsedData.email,
                    tel = parsedData.phone,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated
                )
            }
            is QrResult.Geolocation -> {
                Geolocation(
                    id = id,
                    latitude = parsedData.latitude,
                    longitude = parsedData.longitude,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated
                )
            }
            is QrResult.Link -> {
                Url(
                    id = id,
                    link = parsedData.url,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated
                )
            }
            is QrResult.PhoneNumber -> {
                PhoneNumber(
                    id = id,
                    phoneNumber = parsedData.phoneNumber,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated
                )
            }
            is QrResult.PlainText -> {
                PlainText(
                    id = id,
                    text = parsedData.text,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated
                )
            }
            is QrResult.Wifi -> {
                Wifi(
                    id = id,
                    ssid = parsedData.ssid,
                    password = parsedData.password,
                    encryptionType = parsedData.encryptionType,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated
                )
            }
            null -> null
        }
    }

    private fun String.getCreatedAtTime(): String {
        val createdAt = OffsetDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        return createdAt.format(formatter)
    }
}


data class QrHistoryUiState(
    val scannedQrs: List<HistoricQrCode>,
    val generatedQrs: List<HistoricQrCode>
)

sealed class HistoricQrCode {
    open val id: Int = -1
    open val userGenerated: Boolean = false
    open val createdAt: String = ""

    data class ContactDetails(override val id: Int, val name: String, val tel: String, val email: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class Url(override val id: Int, val link: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class Geolocation(override val id: Int, val latitude: String, val longitude: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class PhoneNumber(override val id: Int, val phoneNumber: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class Wifi(override val id: Int, val ssid: String, val password: String, val encryptionType: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class PlainText(override val id: Int, val text: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
}