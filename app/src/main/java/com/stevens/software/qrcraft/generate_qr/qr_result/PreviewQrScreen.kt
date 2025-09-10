package com.stevens.software.qrcraft.generate_qr.qr_result

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.ui.theme.extendedColours
import com.stevens.software.qrcraft.ui.toolkit.QrImage
import com.stevens.software.qrcraft.ui.toolkit.QrInfo
import com.stevens.software.qrcraft.ui.toolkit.TopNavBar

@Composable
fun PreviewQrScreen(
    viewModel: PreviewQrViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    PreviewQrView(
        qrCodeBitmap = uiState.value.bitmap,
        qrCodeData = uiState.value.qrData,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun PreviewQrView(
    qrCodeBitmap: Bitmap?,
    qrCodeData: QrCodeData?,
    onNavigateBack: () -> Unit
){
    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            TopNavBar(
                title = stringResource(R.string.scan_result),
                backgroundColor = MaterialTheme.colorScheme.onSurface,
                iconColour = MaterialTheme.extendedColours.onOverlay,
                titleColour = MaterialTheme.extendedColours.onOverlay,
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(top = 44.dp),
        ) {
            QrInfo(
                qrCodeData = qrCodeData,
                onShare = {},
                onCopyToClipboard = {})
            QrImage(qrCodeBitmap)
        }
    }
}