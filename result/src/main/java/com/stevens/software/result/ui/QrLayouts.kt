package com.stevens.software.result.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.uitoolkit.R
import com.stevens.software.uitoolkit.theme.extendedColours


@Composable
internal fun PlainText(qrCodeData: QrCodeData.PlainText) {
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
internal fun PhoneNumber(qrCodeData: QrCodeData.PhoneNumber) {
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
internal fun Geolocation(qrCodeData: QrCodeData.Geolocation) {
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
internal fun ContactDetails(qrCodeData: QrCodeData.ContactDetails) {
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
internal fun Link(qrCodeData: QrCodeData.Url) {
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
internal fun Wifi(qrCodeData: QrCodeData.Wifi) {
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