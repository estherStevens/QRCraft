package com.stevens.software.qrcraft.generate_qr.data_entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.generate_qr.select_type.QrType
import com.stevens.software.qrcraft.qr_camera.BitmapAnalyzer
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QrDataEntryViewModel(
    qrCodeType: QrType?,
    private val qrGeneratorRepository: QrGeneratorRepository,
    private val qrCodeAnalyzer: BitmapAnalyzer
): ViewModel() {

    private val _isError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _qrData: MutableStateFlow<QrData?> = MutableStateFlow(null)

    private val _navigationEvents: MutableSharedFlow<QrDataEntryNavigationEvents> = MutableSharedFlow()
    val navigationEvents = _navigationEvents.asSharedFlow()

    val uiState: StateFlow<QrDataEntryUiState> = combine(
        _qrData,
        _isLoading,
        _isError)
    { qrData, isLoading, isError ->
        QrDataEntryUiState(
            screenTitle = qrData?.setScreenTitle() ?: 0,
            qrData = qrData,
            isLoading = isLoading,
            isError = isError,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        QrDataEntryUiState(screenTitle = 0, qrData = null, isLoading = true, isError = false)
    )

    init {
        if(qrCodeType == null){
            _isError.update { true }
            _isLoading.update { false }
        } else {
            _qrData.update { qrCodeType.setQrDataDefaultState() }
            _isLoading.update { false }
        }

    }

    fun onNavigateBack() {
        viewModelScope.launch {
            _navigationEvents.emit(QrDataEntryNavigationEvents.NavigateBack)
        }
    }

    fun onFieldChange(value: QrTypeFieldChange){
        val currentDataEntry = uiState.value.qrData
        _qrData.update {
            when(value){
                is QrTypeFieldChange.ContactEmailChanged -> (currentDataEntry as? QrData.Contact)?.copy(email = value.email)
                is QrTypeFieldChange.ContactNameChanged -> (currentDataEntry as? QrData.Contact)?.copy(name = value.name)
                is QrTypeFieldChange.ContactPhoneNumberChanged -> (currentDataEntry as? QrData.Contact)?.copy(phoneNumber = value.phoneNumber)
                is QrTypeFieldChange.GeolocationLatitudeChanged -> (currentDataEntry as? QrData.Geolocation)?.copy(latitude = value.latitude)
                is QrTypeFieldChange.GeolocationLongitudeChanged -> (currentDataEntry as? QrData.Geolocation)?.copy(longitude = value.longitude)
                is QrTypeFieldChange.LinkChanged -> (currentDataEntry as? QrData.Link)?.copy(url = value.url)
                is QrTypeFieldChange.PhoneNumberChanged -> (currentDataEntry as? QrData.PhoneNumber)?.copy(phoneNumber = value.phoneNumber)
                is QrTypeFieldChange.TextChanged -> (currentDataEntry as? QrData.Text)?.copy(text = value.text)
                is QrTypeFieldChange.WifiEncryptionTypeChanged -> (currentDataEntry as? QrData.Wifi)?.copy(encryptionType = value.encryptionType)
                is QrTypeFieldChange.WifiPasswordChanged -> (currentDataEntry as? QrData.Wifi)?.copy(password = value.password)
                is QrTypeFieldChange.WifiSsidChanged -> (currentDataEntry as? QrData.Wifi)?.copy(ssid = value.ssid)
            }
        }
    }

    fun generateQrCode() {
        uiState.value.qrData?.let {
            when(it){
                is QrData.Contact ->  createQrCode("${it.name}${it.email}${it.phoneNumber}")
                is QrData.Geolocation -> createQrCode("${it.longitude}${it.latitude}")
                is QrData.Link -> createQrCode(it.url)
                is QrData.PhoneNumber -> createQrCode(it.phoneNumber)
                is QrData.Text -> createQrCode(it.text)
                is QrData.Wifi -> createQrCode("${it.ssid}${it.password}${it.encryptionType}}")
            }
        }
    }


    private fun createQrCode(qrData: String){
        val bitmap = qrGeneratorRepository.createQrCode(qrData)

        qrCodeAnalyzer.analyzeFromBitmap(bitmap, onResult = {
            viewModelScope.launch {
                _navigationEvents.emit(QrDataEntryNavigationEvents.NavigateToPreviewQrScreen(qrCodeBitmapFilePath = it.first, qrData = it.second))
            }
        })

    }


    private fun QrData.setScreenTitle() =
        when(this) {
            is QrData.Contact -> R.string.create_qr_contact
            is QrData.Geolocation -> R.string.create_qr_geolocation
            is QrData.Link -> R.string.create_qr_link
            is QrData.PhoneNumber -> R.string.create_qr_phone_number
            is QrData.Text -> R.string.create_qr_text
            is QrData.Wifi -> R.string.create_qr_wifi
        }

    private fun QrType.setQrDataDefaultState() =
        when(this){
            QrType.TEXT -> QrData.Text(text = "")
            QrType.LINK -> QrData.Link(url = "")
            QrType.CONTACT -> QrData.Contact(name = "", phoneNumber = "", email = "")
            QrType.PHONE_NUMBER -> QrData.PhoneNumber(phoneNumber = "")
            QrType.GEOLOCATION -> QrData.Geolocation(longitude = "", latitude = "")
            QrType.WIFI -> QrData.Wifi(ssid = "", password = "", encryptionType = "")
        }
}

data class QrDataEntryUiState(
    val screenTitle: Int,
    val qrData: QrData?,
    val isLoading: Boolean,
    val isError: Boolean,
)

sealed class QrDataEntryNavigationEvents {
    object NavigateBack : QrDataEntryNavigationEvents()
    data class NavigateToPreviewQrScreen(val qrCodeBitmapFilePath: String, val qrData: QrCodeData?): QrDataEntryNavigationEvents()
}

sealed class QrData{
    data class Text(val text: String): QrData()
    data class Link(val url: String): QrData()
    data class Contact(val name: String, val phoneNumber: String, val email: String): QrData()
    data class PhoneNumber(val phoneNumber: String): QrData()
    data class Geolocation(val latitude: String, val longitude: String): QrData()
    data class Wifi(val ssid: String, val password: String, val encryptionType: String): QrData()
}

sealed class QrTypeFieldChange(){
    data class TextChanged(val text: String): QrTypeFieldChange()
    data class LinkChanged(val url: String): QrTypeFieldChange()
    data class ContactNameChanged(val name: String): QrTypeFieldChange()
    data class ContactPhoneNumberChanged(val phoneNumber: String): QrTypeFieldChange()
    data class ContactEmailChanged(val email: String): QrTypeFieldChange()
    data class PhoneNumberChanged(val phoneNumber: String): QrTypeFieldChange()
    data class GeolocationLatitudeChanged(val latitude: String): QrTypeFieldChange()
    data class GeolocationLongitudeChanged(val longitude: String): QrTypeFieldChange()
    data class WifiSsidChanged(val ssid: String): QrTypeFieldChange()
    data class WifiPasswordChanged(val password: String): QrTypeFieldChange()
    data class WifiEncryptionTypeChanged(val encryptionType: String): QrTypeFieldChange()
}