package com.stevens.software.qrcraft.generate_qr.preview_qr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.qrcraft.qr_camera.BitmapAnalyzer
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
    qrCodeBitmapFilePath: String,
    private val bitmapAnalyzer: BitmapAnalyzer
) : ViewModel() {

    private val _qrBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _qrData: MutableStateFlow<QrCodeData?> = MutableStateFlow(null)

    val uiState: StateFlow<GeneratedQrResultUiState> = combine(
        _qrBitmap,
        _qrData,
        _isLoading
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
        decodeBitmap(qrCodeBitmapFilePath)
    }

    private fun decodeBitmap(qrCodeBitmapFilePath: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val bitmap = BitmapFactory.decodeFile(qrCodeBitmapFilePath)
                val qrData = bitmapAnalyzer.extractDataFromQr(bitmap)
                _qrBitmap.emit(bitmap)
                _qrData.emit(qrData)
                _isLoading.emit(false)
            }
        }

    }
}

data class GeneratedQrResultUiState(
    val bitmap: Bitmap?,
    val qrData: QrCodeData?,
    val isLoading: Boolean
)
