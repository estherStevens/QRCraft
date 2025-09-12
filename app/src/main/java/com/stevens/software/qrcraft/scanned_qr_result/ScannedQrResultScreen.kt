package com.stevens.software.qrcraft.scanned_qr_result

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.theme.extendedColours
import com.stevens.software.qrcraft.ui.toolkit.QrImage
import com.stevens.software.qrcraft.ui.toolkit.QrInfo
import com.stevens.software.qrcraft.ui.toolkit.TopNavBar

@Composable
fun QrResultScreen(
    viewModel: QrResultViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    QrResultView(
        isLoading = uiState.value.isLoading,
        qrCodeBitmap = uiState.value.qrCodeBitmap,
        qrCodeData = uiState.value.qrDataType,
        onNavigateBack = onNavigateBack,
        onShare = {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, it)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        },
        onCopyToClipboard = {
            clipboardManager.setText(AnnotatedString(it))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrResultView(
    isLoading: Boolean,
    qrCodeBitmap: Bitmap?,
    qrCodeData: QrCodeData?,
    onNavigateBack: () -> Unit,
    onShare: (String) -> Unit,
    onCopyToClipboard: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            TopNavBar(
                title = stringResource(R.string.scan_result),
                backgroundColor = MaterialTheme.colorScheme.onSurface,
                titleColour = MaterialTheme.extendedColours.onOverlay,
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_icon),
                            tint = MaterialTheme.extendedColours.onOverlay,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.onSurface)
        ) {
            when(isLoading){
                true -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                }
                false -> {
                    Spacer(Modifier.size(44.dp))
                    QrInfo(
                        qrCodeData = qrCodeData,
                        onShare = onShare,
                        onCopyToClipboard = onCopyToClipboard)
                    QrImage(qrCodeBitmap)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    QRCraftTheme {
        QrResultView(
            isLoading = false,
            qrCodeBitmap = null,
            qrCodeData = QrCodeData.PlainText("Some Text Here"),
            onNavigateBack = {},
            onShare = {},
            onCopyToClipboard = {}
        )
    }
}