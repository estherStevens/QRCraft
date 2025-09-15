package com.stevens.software.qrcraft.scanned_qr_result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.stevens.software.qrcraft.qr_camera.QrCodeAnalyzer
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.qrcraft.db.QrCodeRepository
import com.stevens.software.qrcraft.db.QrResult
import com.stevens.software.result.ui.GeneratedQrResultUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrResultViewModel(
    qrCodeId: Long,
    private val qrCodeRepository: QrCodeRepository
) : ViewModel() {

    private val _qrBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _qrData: MutableStateFlow<QrCodeData?> = MutableStateFlow(null)

    val uiState: StateFlow<ScanResultUiState> = combine(
        _qrBitmap,
        _qrData,
        _isLoading,
    ) { bitmap, qrData, isLoading->
        ScanResultUiState(
            bitmap = bitmap,
            qrData = qrData,
            isLoading = false
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        ScanResultUiState(null, null, true)
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


    private fun QrResult.toQrCodeData(qrBitmapPath: String): QrCodeData? =
        when(this){ //todo - ideally dont need to the bitmap path here
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

data class ScanResultUiState(
    val bitmap: Bitmap?,
    val qrData: QrCodeData?,
    val isLoading: Boolean
)
