package com.stevens.software.qrcraft.generate_qr.qr_result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.scanned_qr_result.data.QrDataParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreviewQrViewModel(
    qrScanResult: String,
    qrCodeBitmapFilePath: String,
    private val qrDataParser: QrDataParser,
) : ViewModel() {

    private val _qrBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    private val qrBitmap = _qrBitmap.asStateFlow()

    val uiState: StateFlow<GeneratedQrResultUiState> = combine(
        qrBitmap,
        qrDataParser.qrData,
    ) { bitmap, qrData ->
        if(bitmap != null){
            GeneratedQrResultUiState(
                bitmap = bitmap,
                qrData = qrData,
                isError = false
            )
        } else {
            GeneratedQrResultUiState(
                bitmap = null,
                qrData = null,
                isError = true
            )
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        GeneratedQrResultUiState(null, null, false)
    )

    init {
        qrDataParser.parseQrData(qrScanResult)
        decodeBitmap(qrCodeBitmapFilePath)
    }

    private fun decodeBitmap(qrCodeBitmapFilePath: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val bitmap = BitmapFactory.decodeFile(qrCodeBitmapFilePath)
                _qrBitmap.update { bitmap }
            }
        }

    }
}

data class GeneratedQrResultUiState(
    val bitmap: Bitmap?,
    val qrData: QrCodeData?,
    val isError: Boolean
)
