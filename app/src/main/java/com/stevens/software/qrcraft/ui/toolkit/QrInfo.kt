package com.stevens.software.qrcraft.ui.toolkit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.ui.theme.extendedColours

@Composable
fun QrInfo(
    qrCodeData: QrCodeData?,
    onShare: (String) -> Unit,
    onCopyToClipboard: (String) -> Unit
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
                onShare = { onShare(extractDataAndConvertToString(qrCodeData)) },
                onCopy = { onCopyToClipboard(extractDataAndConvertToString(qrCodeData)) }
            )
            Spacer(Modifier.size(16.dp))
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

private fun extractDataAndConvertToString(qrCodeData: QrCodeData): String =
    when(qrCodeData){
        is QrCodeData.ContactDetails -> buildString {
            appendLine(qrCodeData.name)
            appendLine(qrCodeData.tel)
            appendLine(qrCodeData.email)
        }
        is QrCodeData.Geolocation -> buildString {
            append(qrCodeData.latitude)
            append(", ")
            append(qrCodeData.longitude)
        }
        is QrCodeData.PhoneNumber -> qrCodeData.phoneNumber
        is QrCodeData.PlainText -> qrCodeData.text
        is QrCodeData.Url -> qrCodeData.link
        is QrCodeData.Wifi -> buildString {
            appendLine(qrCodeData.ssid)
            appendLine(qrCodeData.password)
            appendLine(qrCodeData.encryptionType)
        }
    }

