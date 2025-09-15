package com.stevens.software.qrcraft.generate_qr.preview_qr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.qrcraft.db.QrCodeRepository
import com.stevens.software.qrcraft.db.QrResult
import com.stevens.software.qrcraft.qr_camera.QrCodeAnalyzer
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreviewQrViewModel(
    qrCodeId: Long,
    private val qrCodeAnalyzer: QrCodeAnalyzer,
    private val qrCodeRepository: QrCodeRepository
) : ViewModel() {

    private val _qrBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _qrData: MutableStateFlow<QrCodeData?> = MutableStateFlow(null)

    val uiState: StateFlow<GeneratedQrResultUiState> = combine(
        _qrBitmap,
        _qrData,
        _isLoading,
    ) { bitmap, qrData, isLoading->
        GeneratedQrResultUiState(
            bitmap = bitmap,
            qrData = qrData,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        GeneratedQrResultUiState(null, null, true)
    )

    init {
        viewModelScope.launch {
            qrCodeRepository.getQrCode(qrCodeId).collect { qrCode ->
                qrCode.let {
                    val bitmap =  BitmapFactory.decodeFile(it?.qrBitmapPath)
                    _qrBitmap.emit(bitmap)
                    _qrData.emit(it?.parsedData?.toQrCodeData())
                }
            }
        }
    }

    private fun QrResult.toQrCodeData(): QrCodeData? =
        when(this){
            is QrResult.Contact -> {
                QrCodeData.ContactDetails(name = this.name, email = this.email, tel = this.phone)
            }
            is QrResult.Geolocation -> {
                QrCodeData.Geolocation(longitude = this.longitude, latitude = this.latitude)
            }
            is QrResult.Link -> {
                QrCodeData.Url(link = this.url)
            }
            is QrResult.PhoneNumber -> {
                QrCodeData.PhoneNumber(phoneNumber = this.phoneNumber)
            }
            is QrResult.PlainText -> {
                QrCodeData.PlainText(text = this.text)
            }
            is QrResult.Wifi -> {
                QrCodeData.Wifi(ssid = this.ssid, password = this.password, encryptionType = this.encryptionType)
            }
        }
}

data class GeneratedQrResultUiState(
    val bitmap: Bitmap?,
    val qrData: QrCodeData?,
    val isLoading: Boolean
)
