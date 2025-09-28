package com.stevens.software.result.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.core.QrCodeRepository
import com.stevens.software.core.QrResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PreviewQrViewModel(
    qrCodeId: Long,
    private val qrCodeRepository: QrCodeRepository
) : ViewModel() {

    private val _qrBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _qrData: MutableStateFlow<QrCodeData?> = MutableStateFlow(null)
    private val _isFavourite: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val uiState: StateFlow<GeneratedQrResultUiState> = combine(
        _qrBitmap,
        _qrData,
        _isLoading,
    ) { bitmap, qrData, isLoading->
        GeneratedQrResultUiState(
            bitmap = bitmap,
            qrData = qrData,
            isLoading = false,
            isFavourite = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        GeneratedQrResultUiState(bitmap = null, qrData = null, isLoading = true, isFavourite = false)
    )

    init {
        viewModelScope.launch {
            qrCodeRepository.getQrCode(qrCodeId).collect { qrCode ->
                qrCode.let {
                    val qrData = it?.parsedData?.toQrCodeData(it.qrBitmapPath)
                    val bitmap = BitmapFactory.decodeFile(qrData?.qrBitmapPath)
                    _qrBitmap.emit(bitmap)
                    _qrData.emit(qrData)
                }
            }
        }
    }

    fun updateFavouriteState(isFavourite: Boolean){
        viewModelScope.launch {
//            qrCodeRepository.updateFavouriteStatus(
//                id = _qrData.value.
//            )
        }
    }

    private fun QrResult.toQrCodeData(qrBitmapPath: String): QrCodeData? =
        when(this){
            is QrResult.Contact -> {
                QrCodeData.ContactDetails(qrBitmapPath = qrBitmapPath, name = this.name, email = this.email, tel = this.phone)
            }
            is QrResult.Geolocation -> {
                QrCodeData.Geolocation(qrBitmapPath = qrBitmapPath, longitude = this.longitude, latitude = this.latitude)
            }
            is QrResult.Link -> {
                QrCodeData.Url(qrBitmapPath = qrBitmapPath, link = this.url)
            }
            is QrResult.PhoneNumber -> {
                QrCodeData.PhoneNumber(qrBitmapPath = qrBitmapPath, phoneNumber = this.phoneNumber)
            }
            is QrResult.PlainText -> {
                QrCodeData.PlainText(qrBitmapPath = qrBitmapPath, text = this.text)
            }
            is QrResult.Wifi -> {
                QrCodeData.Wifi(qrBitmapPath = qrBitmapPath, ssid = this.ssid, password = this.password, encryptionType = this.encryptionType)
            }
        }
}

data class GeneratedQrResultUiState(
    val bitmap: Bitmap?,
    val qrData: QrCodeData?,
    val isLoading: Boolean,
    val isFavourite: Boolean
)
