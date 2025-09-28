package com.stevens.software.generator.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.analyzer.AnalyzedQrCodeData
import com.stevens.software.generator.QrType
import com.stevens.software.generator.data.QrGeneratorRepository
import com.stevens.software.core.QrCode
import com.stevens.software.core.QrCodeRepository
import com.stevens.software.core.QrResult
import com.stevens.software.analyzer.QrCodeAnalyzer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

@RequiresApi(Build.VERSION_CODES.O)
class QrDataEntryViewModel(
    qrCodeType: QrType?,
    private val qrGeneratorRepository: QrGeneratorRepository,
    private val qrCodeAnalyzer: QrCodeAnalyzer,
    private val qrCodeRepository: QrCodeRepository
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
            qrData = qrData,
            isLoading = isLoading,
            isError = isError,
            isFormValid = qrData?.isInputValid() == true
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        QrDataEntryUiState(qrData = null, isLoading = true, isError = false, isFormValid = false)
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

    fun generateQrCode(){
        uiState.value.qrData?.let {
            createQrCode(it)
        }
    }

    private fun createQrCode(qrData: QrData){
        viewModelScope.launch {
            val bitmap = qrGeneratorRepository.createQrCode(qrData)
            val qrCodeData = qrCodeAnalyzer.extractDataFromQr(bitmap)
            qrCodeData?.let {
                val qrCode = when(qrCodeData){
                    is AnalyzedQrCodeData.ContactDetails -> QrCode( //todo maybe move this logic to the core module
                        qrBitmapPath = qrCodeData.qrBitmapPath,
                        parsedData = QrResult.Contact(
                            name = qrCodeData.name,
                            email = qrCodeData.email,
                            phone = qrCodeData.tel
                        ),
                        dateCreated = OffsetDateTime.now().toString(),
                        userGenerated = true
                    )
                    is AnalyzedQrCodeData.Geolocation -> QrCode(
                        qrBitmapPath = qrCodeData.qrBitmapPath,
                        parsedData = QrResult.Geolocation(longitude = qrCodeData.longitude, latitude = qrCodeData.latitude),
                        dateCreated = OffsetDateTime.now().toString(),
                        userGenerated = true
                    )
                    is AnalyzedQrCodeData.PhoneNumber -> QrCode(
                        qrBitmapPath = qrCodeData.qrBitmapPath,
                        parsedData = QrResult.PhoneNumber(phoneNumber = qrCodeData.phoneNumber),
                        dateCreated = OffsetDateTime.now().toString(),
                        userGenerated = true
                    )
                    is AnalyzedQrCodeData.PlainText -> QrCode(
                        qrBitmapPath = qrCodeData.qrBitmapPath,
                        parsedData = QrResult.PlainText(text = qrCodeData.text),
                        dateCreated = OffsetDateTime.now().toString(),
                        userGenerated = true
                    )
                    is AnalyzedQrCodeData.Url -> QrCode(
                        qrBitmapPath = qrCodeData.qrBitmapPath,
                        parsedData = QrResult.Link(url = qrCodeData.link),
                        dateCreated = OffsetDateTime.now().toString(),
                        userGenerated = true
                    )
                    is AnalyzedQrCodeData.Wifi -> QrCode(
                        qrBitmapPath = qrCodeData.qrBitmapPath,
                        parsedData = QrResult.Wifi(ssid = qrCodeData.ssid, password = qrCodeData.password, encryptionType = qrCodeData.encryptionType),
                        dateCreated = OffsetDateTime.now().toString(),
                        userGenerated = true
                    )
                }
                val id = qrCodeRepository.insertQrCode(qrCode)
                _navigationEvents.emit(QrDataEntryNavigationEvents.NavigateToPreviewQrScreen(qrCodeId = id))
            }
        }
    }

    fun QrData?.isInputValid(): Boolean {
        return when(this){
            is QrData.Contact -> name.isNotBlank() && phoneNumber.isNotBlank() && email.isNotBlank()
            is QrData.Geolocation -> longitude.isNotBlank() && latitude.isNotBlank()
            is QrData.Link -> url.isNotBlank()
            is QrData.PhoneNumber -> phoneNumber.isNotBlank()
            is QrData.Text -> text.isNotBlank()
            is QrData.Wifi -> ssid.isNotBlank() && encryptionType.isNotBlank() && password.isNotBlank()
            else -> false
        }
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
    val qrData: QrData?,
    val isLoading: Boolean,
    val isError: Boolean,
    val isFormValid: Boolean,
)

sealed class QrDataEntryNavigationEvents {
    object NavigateBack : QrDataEntryNavigationEvents()
    data class NavigateToPreviewQrScreen(val qrCodeId: Long): QrDataEntryNavigationEvents()
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