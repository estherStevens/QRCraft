package com.stevens.software.qrcraft.qr_camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraViewModel: ViewModel() {
    private val _snackBar: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val snackBar = _snackBar.asSharedFlow()

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
}

data class QrCameraUiState(
    val isLoading: Boolean
)