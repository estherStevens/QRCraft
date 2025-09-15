package com.stevens.software.result.ui

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.uitoolkit.R
import com.stevens.software.uitoolkit.theme.extendedColours
import com.stevens.software.uitoolkit.toolkit.TopNavBar

@Composable
fun PreviewQrScreen(
    viewModel: PreviewQrViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    PreviewQrView(
        qrCodeBitmap = uiState.value.bitmap,
        qrCodeData = uiState.value.qrData,
        onNavigateBack = onNavigateBack,
        onShare = {
//            val sendIntent = Intent().apply {
//                Intent.setAction = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_TEXT, it)
//                Intent.setType = "text/plain"
//            }
//            val shareIntent = Intent.createChooser(sendIntent, null)
//            context.startActivity(shareIntent)
        },
        onCopyToClipboard = {
            clipboardManager.setText(AnnotatedString(it))
        }
    )
}

@Composable
fun PreviewQrView(
    qrCodeBitmap: Bitmap?,
    qrCodeData: QrCodeData?,
    onNavigateBack: () -> Unit,
    onShare: (String) -> Unit,
    onCopyToClipboard: (String) -> Unit
){
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
                .padding(top = 44.dp),
        ) {
            QrInfo(
                qrCodeData = qrCodeData,
                onShare = onShare,
                onCopyToClipboard = onCopyToClipboard)
            QrImage(qrCodeBitmap)
        }
    }
}