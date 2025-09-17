package com.stevens.software.scanner.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.core.QrCode
import com.stevens.software.core.QrCodeRepository
import com.stevens.software.core.QrResult
import com.stevens.software.analyzer.QrCodeAnalyzer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class CameraViewModel(
    private val qrCodeAnalyzer: QrCodeAnalyzer,
    private val qrCodeRepository: QrCodeRepository
): ViewModel() {

    private val _snackBar: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val snackBar = _snackBar.asSharedFlow()

    private val _navigationEvents: MutableSharedFlow<CameraNavigationEvents> = MutableSharedFlow()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val uiState = isLoading.map {
        QrCameraUiState(
            isLoading = it
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        QrCameraUiState(false)
    )

    fun showPermissionGrantedSnackBar(){
        viewModelScope.launch {
            _snackBar.emit(true)
        }
    }

    fun onQrCodeDetected(){
        _isLoading.update {
            true
        }
    }

    fun onQrScanned(qrCodeBitmap: Bitmap?){
        viewModelScope.launch {
            if(qrCodeBitmap != null){
               val qrCodeData = qrCodeAnalyzer.extractDataFromQr(qrCodeBitmap)
                qrCodeData.let {
                    qrCodeData?.let {
                        val qrCode = when(qrCodeData){
                            is QrCodeData.ContactDetails -> QrCode( //todo maybe move this logic to the core module
                                qrBitmapPath = qrCodeData.qrBitmapPath,
                                parsedData = QrResult.Contact(
                                    name = qrCodeData.name,
                                    email = qrCodeData.email,
                                    phone = qrCodeData.tel
                                ),
                                dateCreated = OffsetDateTime.now().toString(),
                                userGenerated = false
                            )
                            is QrCodeData.Geolocation -> QrCode(
                                qrBitmapPath = qrCodeData.qrBitmapPath,
                                parsedData = QrResult.Geolocation(longitude = qrCodeData.longitude, latitude = qrCodeData.latitude),
                                dateCreated = OffsetDateTime.now().toString(),
                                userGenerated = false
                            )
                            is QrCodeData.PhoneNumber -> QrCode(
                                qrBitmapPath = qrCodeData.qrBitmapPath,
                                parsedData = QrResult.PhoneNumber(phoneNumber = qrCodeData.phoneNumber),
                                dateCreated = OffsetDateTime.now().toString(),
                                userGenerated = false
                            )
                            is QrCodeData.PlainText -> QrCode(
                                qrBitmapPath = qrCodeData.qrBitmapPath,
                                parsedData = QrResult.PlainText(text = qrCodeData.text),
                                dateCreated = OffsetDateTime.now().toString(),
                                userGenerated = false
                            )
                            is QrCodeData.Url -> QrCode(
                                qrBitmapPath = qrCodeData.qrBitmapPath,
                                parsedData = QrResult.Link(url = qrCodeData.link),
                                dateCreated = OffsetDateTime.now().toString(),
                                userGenerated = false
                            )
                            is QrCodeData.Wifi -> QrCode(
                                qrBitmapPath = qrCodeData.qrBitmapPath,
                                parsedData = QrResult.Wifi(ssid = qrCodeData.ssid, password = qrCodeData.password, encryptionType = qrCodeData.encryptionType),
                                dateCreated = OffsetDateTime.now().toString(),
                                userGenerated = false
                            )
                        }
                        val id = qrCodeRepository.insertQrCode(qrCode)
                        _navigationEvents.emit(CameraNavigationEvents.OnNavigateToPreviewQr(id)) }
                }
            }
        }
    }

}

data class QrCameraUiState(
    val isLoading: Boolean,
)

sealed interface CameraNavigationEvents{
    data class OnNavigateToPreviewQr(val qrCodeId: Long) : CameraNavigationEvents
}