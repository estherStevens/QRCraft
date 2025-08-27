package com.stevens.software.qrcraft.generate_qr.data_entry

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.stevens.software.qrcraft.generate_qr.select_type.QrType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QrDataEntryViewModel(
    qrCodeType: QrType
): ViewModel() {

    private val _uiState: MutableStateFlow<QrDataEntryUiState> = MutableStateFlow(QrDataEntryUiState(null, ""))
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                qrCodeType = qrCodeType
            )
        }
    }

    fun updateValue(value: String){
        _uiState.update {
            it.copy(
                value = value
            )
        }
    }
}

data class QrDataEntryUiState(
    val qrCodeType: QrType?,
    val value: String,

)