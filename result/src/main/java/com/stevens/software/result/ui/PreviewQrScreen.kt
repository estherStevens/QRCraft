package com.stevens.software.result.ui

import android.content.Intent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.result.data.PreviewQrCodeData
import com.stevens.software.uitoolkit.R
import com.stevens.software.uitoolkit.theme.QRCraftTheme
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
        },
        onFavouriteToggle = {
            viewModel.updateFavouriteState(it)
        }
    )
}

@Composable
fun PreviewQrView(
    qrCodeBitmap: Bitmap?,
    qrCodeData: PreviewQrCodeData?,
    onNavigateBack: () -> Unit,
    onShare: (String) -> Unit,
    onCopyToClipboard: (String) -> Unit,
    onFavouriteToggle: (Boolean) -> Unit
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
                },
                trailingIcon = {
                    Favourite(
                        isFavourite = qrCodeData?.isFavourite ?: false,
                        onFavouriteToggle = onFavouriteToggle
                    )
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

@Composable
private fun Favourite(
    isFavourite: Boolean,
    onFavouriteToggle: (Boolean) -> Unit
){
    val favouriteIcon = when(isFavourite) {
        true -> painterResource(R.drawable.favourited_icon)
        false -> painterResource(R.drawable.favourite_icon)
    }
    val contentDescription = when(isFavourite) {
        true -> stringResource(R.string.qr_unfavourite)
        false -> stringResource(R.string.qr_favourite)
    }
    IconButton(
        onClick = {
            onFavouriteToggle(isFavourite.not())
        }
    ) {
        Icon(
            painter = favouriteIcon,
            tint = MaterialTheme.extendedColours.onOverlay,
            contentDescription = contentDescription
        )
    }
}

@Preview
@Composable
private fun PreviewQrViewPreview(@PreviewParameter(PreviewQrPreviewParameterProvider::class) qrCodeData: PreviewQrCodeData){
    QRCraftTheme {
        PreviewQrView(
            qrCodeBitmap = null,
            qrCodeData = qrCodeData,
            onNavigateBack = {},
            onShare = {},
            onCopyToClipboard = {},
            onFavouriteToggle = {}
        )
    }
}

class PreviewQrPreviewParameterProvider(): PreviewParameterProvider<PreviewQrCodeData> {
    override val values = sequenceOf(
        PreviewQrCodeData.PhoneNumber(
            id = 1,
            qrBitmapPath = "",
            phoneNumber = "",
            isFavourite = false
        ),
        PreviewQrCodeData.Url(
            id = 2,
            qrBitmapPath = "",
            link = "",
            isFavourite = false
        ),
        PreviewQrCodeData.PlainText(
            id = 3,
            qrBitmapPath = "",
            text = "",
            isFavourite = true
        ),
        PreviewQrCodeData.Geolocation(
            id = 4,
            qrBitmapPath = "",
            longitude = "",
            latitude = "",
            isFavourite = true
        ),
        PreviewQrCodeData.ContactDetails(
            id = 5,
            qrBitmapPath = "",
            name = "",
            tel = "",
            email = "",
            isFavourite = false
        ),
        PreviewQrCodeData.Wifi(
            id = 6,
            qrBitmapPath = "",
            ssid = "",
            password = "",
            encryptionType = "",
            isFavourite = false
        ),
    )
}