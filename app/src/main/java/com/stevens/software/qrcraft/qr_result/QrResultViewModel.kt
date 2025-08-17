package com.stevens.software.qrcraft.qr_result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QrResultViewModel(
    qrScanResult: String,
    qrCodeBitmapFilePath: String
) : ViewModel() {

    private val _uiState: MutableStateFlow<ScanResultUiState> = MutableStateFlow(ScanResultUiState(qrCodeBitmap = null))
    val uiState = _uiState.asStateFlow()

    init {
        val bitmap = BitmapFactory.decodeFile(qrCodeBitmapFilePath)
        _uiState.update {
            ScanResultUiState(bitmap)
        }
    }
}

data class ScanResultUiState(
    val qrCodeBitmap: Bitmap?
)