package com.stevens.software.qrcraft.qr_result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.qr_result.data.QrDataParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrResultViewModel(
    qrScanResult: String,
    qrCodeBitmapFilePath: String,
    private val qrDataParser: QrDataParser
) : ViewModel() {

    private val _qrBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    val qrBitmap = _qrBitmap.asStateFlow()

    init {
        qrDataParser.parseQrData(qrScanResult)
        decodeBitmap(qrCodeBitmapFilePath)
    }

    val uiState = combine(
        qrDataParser.qrData,
        qrBitmap
    ) { qrType, qrBitmap ->
        ScanResultUiState(
            qrCodeBitmap = qrBitmap,
            qrDataType = qrType,
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
                _qrBitmap.update {
                    bitmap
                }
            }
        }

    }

    private fun initialScanResultUiState() =
        ScanResultUiState(
            qrCodeBitmap = null,
            qrDataType = null,
        )
}

data class ScanResultUiState(
    val qrCodeBitmap: Bitmap?,
    val qrDataType: QrCodeData?,
)
