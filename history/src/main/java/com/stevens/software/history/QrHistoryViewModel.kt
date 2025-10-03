package com.stevens.software.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            scannedQrs = allQrs.filterNot { it.userGenerated }.sortedByDescending { it.isFavourite },
            generatedQrs = allQrs.filter { it.userGenerated }.sortedByDescending { it.isFavourite }
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

    fun updateFavouriteState(id: Int, isFavourite: Boolean){
        viewModelScope.launch {
            qrCodeRepository.updateFavouriteStatus(
                id = id,
                isFavourite = isFavourite
            )

        }
    }

    private fun QrCode.toHistoricQrCode() : HistoricQrCode? {
        val id = this.id
        val parsedData = this.parsedData
        val createdAt = this.dateCreated
        val isFavourite = this.isFavourite
        return when(parsedData){
            is QrResult.Contact -> {
                ContactDetails(
                    id = id,
                    name = parsedData.name,
                    email = parsedData.email,
                    tel = parsedData.phone,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated,
                    isFavourite = isFavourite
                )
            }
            is QrResult.Geolocation -> {
                Geolocation(
                    id = id,
                    latitude = parsedData.latitude,
                    longitude = parsedData.longitude,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated,
                    isFavourite = isFavourite
                )
            }
            is QrResult.Link -> {
                Url(
                    id = id,
                    link = parsedData.url,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated,
                    isFavourite = isFavourite
                )
            }
            is QrResult.PhoneNumber -> {
                PhoneNumber(
                    id = id,
                    phoneNumber = parsedData.phoneNumber,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated,
                    isFavourite = isFavourite
                )
            }
            is QrResult.PlainText -> {
                PlainText(
                    id = id,
                    text = parsedData.text,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated,
                    isFavourite = isFavourite
                )
            }
            is QrResult.Wifi -> {
                Wifi(
                    id = id,
                    ssid = parsedData.ssid,
                    password = parsedData.password,
                    encryptionType = parsedData.encryptionType,
                    createdAt = createdAt.getCreatedAtTime(),
                    userGenerated = this.userGenerated,
                    isFavourite = isFavourite
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
    open val isFavourite: Boolean = false

    data class ContactDetails(override val id: Int, override val isFavourite: Boolean, val name: String, val tel: String, val email: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class Url(override val id: Int, override val isFavourite: Boolean, val link: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class Geolocation(override val id: Int, override val isFavourite: Boolean, val latitude: String, val longitude: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class PhoneNumber(override val id: Int, override val isFavourite: Boolean, val phoneNumber: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class Wifi(override val id: Int, override val isFavourite: Boolean, val ssid: String, val password: String, val encryptionType: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
    data class PlainText(override val id: Int, override val isFavourite: Boolean, val text: String, override val createdAt: String, override val userGenerated: Boolean) : HistoricQrCode()
}