package com.stevens.software.qrcraft.qr_result

import android.graphics.Bitmap
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.theme.Text
import com.stevens.software.qrcraft.ui.theme.extendedColours

@Composable
fun QrResultScreen(
    viewModel: QrResultViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    QrResultView(
        qrCodeBitmap = uiState.value.qrCodeBitmap,
        qrTypeTile = uiState.value.qrDataType,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrResultView(
    qrCodeBitmap: Bitmap?,
    qrTypeTile: QrCodeData?,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.scan_result),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.extendedColours.onOverlay,
                    )
                },
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
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.onSurface
                )
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
            ScannedQrInfo(qrTypeTile)
            ScannedQrCodeImage(qrCodeBitmap)
        }
    }
}

@Composable
private fun ScannedQrInfo(
    qrCodeData: QrCodeData?,
) {
    if (qrCodeData == null) return
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 100.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.size(70.dp))
            when (qrCodeData) {
                is QrCodeData.ContactDetails -> ContactDetails(qrCodeData)
                is QrCodeData.Geolocation -> Geolocation(qrCodeData)
                is QrCodeData.PhoneNumber -> PhoneNumber(qrCodeData)
                is QrCodeData.PlainText -> PlainText(qrCodeData)
                is QrCodeData.Url -> Link(qrCodeData)
                is QrCodeData.Wifi -> Wifi(qrCodeData)
            }
            Spacer(Modifier.size(24.dp))
            ActionButtons(
                onShare = {},
                onCopy = {}
            )
            Spacer(Modifier.size(16.dp))
        }
    }
}

@Composable
private fun PlainText(qrCodeData: QrCodeData.PlainText) {
    var showMoreIsEnabled by remember { mutableStateOf(false) }
    var shouldShowMoreButton by remember { mutableStateOf(false) }
    val maxLines = if (showMoreIsEnabled) Int.MAX_VALUE else 6

    val showMoreText = when (showMoreIsEnabled) {
        true -> stringResource(R.string.show_less)
        false -> stringResource(R.string.show_more)
    }

    val showMoreTextColour = when (showMoreIsEnabled) {
        true -> MaterialTheme.extendedColours.onSurfaceDisabled
        false -> MaterialTheme.extendedColours.onSurfaceAlt
    }

    Text(
        text = stringResource(R.string.qr_type_text),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.size(20.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = qrCodeData.text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                shouldShowMoreButton = textLayoutResult.hasVisualOverflow
            }
        )
        if(shouldShowMoreButton){
            Spacer(Modifier.size(4.dp))
            Text(
                text = showMoreText,
                style = MaterialTheme.typography.labelLarge,
                color = showMoreTextColour,
                modifier = Modifier.clickable {
                    showMoreIsEnabled = showMoreIsEnabled.not()
                }
            )
        }
    }

}

@Composable
private fun PhoneNumber(qrCodeData: QrCodeData.PhoneNumber) {
    Text(
        text = stringResource(R.string.qr_type_phone_number),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.size(20.dp))
    Text(
        text = qrCodeData.phoneNumber,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

}

@Composable
private fun Geolocation(qrCodeData: QrCodeData.Geolocation) {
    Text(
        text = stringResource(R.string.qr_type_geolocation),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.size(20.dp))
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = MaterialTheme.typography.bodyLarge.toSpanStyle()
                    .copy(color = MaterialTheme.colorScheme.onSurface)
            ) {
                append(qrCodeData.longitude)
                append(", ")
                append(qrCodeData.latitude)
            }
        }
    )
}

@Composable
private fun ContactDetails(qrCodeData: QrCodeData.ContactDetails) {
    Text(
        text = stringResource(R.string.qr_type_contact_details),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.size(20.dp))
    Text(
        text = qrCodeData.name,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = qrCodeData.tel,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = qrCodeData.email,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )

}

@Composable
private fun Link(qrCodeData: QrCodeData.Url) {
    Text(
        text = stringResource(R.string.qr_type_link),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.size(20.dp))
    SelectionContainer {
        Text(
            text = qrCodeData.link,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.background(MaterialTheme.extendedColours.linkBg.copy(alpha = 0.3F))
        )
    }
}

@Composable
private fun Wifi(qrCodeData: QrCodeData.Wifi) {
    Text(
        text = stringResource(R.string.qr_type_wifi),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.size(20.dp))
    Text(
        text = stringResource(R.string.qr_type_wifi_ssid, qrCodeData.ssid),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = stringResource(R.string.qr_type_wifi_password, qrCodeData.password),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = stringResource(R.string.qr_type_wifi_encryption_type, qrCodeData.encryptionType),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ScannedQrCodeImage(
    qrCodeBitmap: Bitmap?
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.extendedColours.surfaceHigher,
                    shape = RoundedCornerShape(16.dp)
                )
                .size(160.dp)
                .padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            qrCodeBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onShare:() -> Unit,
    onCopy: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ShareButton(
            modifier = Modifier.weight(1f),
            onShare = onShare
        )
        CopyButton(
            modifier = Modifier.weight(1f),
            onCopy = onCopy
        )
    }
}

@Composable
private fun ShareButton(
    modifier: Modifier = Modifier,
    onShare: () -> Unit
){
    Button(
        onClick = onShare,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.extendedColours.surfaceHigher
        ),
        shape = CircleShape,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.share_icon),
            contentDescription = stringResource(R.string.qr_result_share_content_description),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.qr_result_share),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CopyButton(
    modifier: Modifier = Modifier,
    onCopy: () -> Unit
){
    Button(
        onClick = onCopy,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.extendedColours.surfaceHigher
        ),
        shape = CircleShape,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.copy_icon),
            contentDescription = stringResource(R.string.qr_result_copy_content_description),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.qr_result_copy),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    QRCraftTheme {
        QrResultView(
            qrCodeBitmap = null,
            qrTypeTile = QrCodeData.PlainText("Some Text Here"),
            onNavigateBack = {}
        )
    }
}