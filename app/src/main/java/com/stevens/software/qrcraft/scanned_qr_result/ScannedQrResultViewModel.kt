package com.stevens.software.qrcraft.scanned_qr_result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.qrcraft.qr_camera.QrCodeAnalyzer
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrResultViewModel(
    qrCodeBitmapFilePath: String,
    private val qrCodeAnalyzer: QrCodeAnalyzer
) : ViewModel() {

    private val _qrBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val qrBitmap = _qrBitmap.asStateFlow()
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val _qrData: MutableStateFlow<QrCodeData?> = MutableStateFlow(null)

    init {
        decodeBitmap(qrCodeBitmapFilePath)
    }

    val uiState = combine(
        _qrData,
        qrBitmap,
        _isLoading
    ) { qrType, qrBitmap, isLoading ->
        ScanResultUiState(
            qrCodeBitmap = qrBitmap,
            qrDataType = qrType,
            isLoading = isLoading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialScanResultUiState()
    )

    private fun decodeBitmap(qrCodeBitmapFilePath: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val bitmap = BitmapFactory.decodeFile(qrCodeBitmapFilePath)
                val qrData = qrCodeAnalyzer.extractDataFromQr(bitmap)
//                _qrBitmap.emit(bitmap)
//                _qrData.emit(qrData)
                _isLoading.emit(false)
            }
        }
    }

    private fun initialScanResultUiState() =
        ScanResultUiState(
            qrCodeBitmap = null,
            qrDataType = null,
            isLoading = true
        )
}

data class ScanResultUiState(
    val qrCodeBitmap: Bitmap?,
    val qrDataType: QrCodeData?,
    val isLoading: Boolean,
)
