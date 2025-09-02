package com.stevens.software.qrcraft.generate_qr.data_entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.generate_qr.select_type.QrCodeType
import com.stevens.software.qrcraft.generate_qr.select_type.QrType
import com.stevens.software.qrcraft.generate_qr.select_type.SelectQrCodeTypeViewModel
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.theme.Text
import com.stevens.software.qrcraft.ui.theme.extendedColours
import com.stevens.software.qrcraft.ui.toolkit.TopNavBar

@Composable
fun QrDataEntryScreen(
    viewModel: QrDataEntryViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.value.isError -> onNavigateBack()
        uiState.value.isLoading -> CircularProgressIndicator()
        else -> {
            QrDataEntryView(
                screenTitle = uiState.value.screenTitle,
                qrTypeDataEntry = uiState.value.qrData!!,
                onValueChange = { value ->
                    viewModel.onFieldChange(value)
                },
                onGenerateQrCode = {
                    viewModel.generateQrCode()
                },
                onNavigateBack = onNavigateBack
            )
        }
    }

}

@Composable
private fun QrDataEntryView(screenTitle: Int,
                            qrTypeDataEntry: QrData,
                            onValueChange: (QrTypeFieldChange) -> Unit,
                            onGenerateQrCode: () -> Unit,
                            onNavigateBack: () -> Unit){
    Scaffold(
        topBar = {
            TopNavBar(
                title = stringResource(screenTitle),
                backgroundColor = MaterialTheme.colorScheme.surface,
                iconColour = MaterialTheme.colorScheme.onSurface,
                titleColour = MaterialTheme.colorScheme.onSurface,
                onNavigateBack = onNavigateBack
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier
            .padding(contentPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.extendedColours.surfaceHigher,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                when(qrTypeDataEntry){
                    is QrData.Contact -> {
                        ContactDataEntry(
                            name = qrTypeDataEntry.name,
                            phoneNumber = qrTypeDataEntry.phoneNumber,
                            email = qrTypeDataEntry.email,
                            onValueChange = onValueChange
                        )
                    }
                    is QrData.Geolocation -> {
                        GeolocationDataEntry(
                            latitude = qrTypeDataEntry.latitude, 
                            longitude = qrTypeDataEntry.longitude,
                            onValueChange = onValueChange
                        )
                    }
                    is QrData.Link -> {
                        LinkDataEntry(
                            url = qrTypeDataEntry.url,
                            onValueChange = onValueChange
                        )
                    }
                    is QrData.PhoneNumber -> {
                        PhoneNumberDataEntry(
                            phoneNumber = qrTypeDataEntry.phoneNumber,
                            onValueChange = onValueChange
                        )
                    }
                    is QrData.Text -> {
                        TextDataEntry(
                            text = qrTypeDataEntry.text,
                            onValueChange = onValueChange,
                        )
                    }
                    is QrData.Wifi -> {
                        WifiDataEntry(
                            ssid = qrTypeDataEntry.ssid,
                            password = qrTypeDataEntry.password,
                            encryptionType = qrTypeDataEntry.encryptionType,
                            onValueChange = onValueChange
                        )
                    }
                }
                Spacer(Modifier.size(16.dp))
                GenerateQrButton(
                    enabled = true,
                    onGenerateQrCode = onGenerateQrCode
                )
            }

        }
    }
}

@Composable
private fun ContactDataEntry(
    name: String,
    phoneNumber: String,
    email: String,
    onValueChange: (QrTypeFieldChange) -> Unit,
) {
    EditTextBox(
        value = name,
        placeholderText = R.string.create_qr_name_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.ContactNameChanged(name = it))
        }
    )
    Spacer(Modifier.size(8.dp))
    EditTextBox(
        value = email,
        placeholderText = R.string.create_qr_email_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.ContactEmailChanged(email = it))
        },
    )
    Spacer(Modifier.size(8.dp))
    EditTextBox(
        value = phoneNumber,
        placeholderText = R.string.create_qr_phone_number_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.ContactPhoneNumberChanged(phoneNumber = it))
        }
    )

}


@Composable
private fun GeolocationDataEntry(
    latitude: String,
    longitude: String,
    onValueChange: (QrTypeFieldChange) -> Unit,
) {
    EditTextBox(
        value = latitude,
        placeholderText = R.string.create_qr_latitude_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.GeolocationLatitudeChanged(latitude = it))
        }
    )
    Spacer(Modifier.size(8.dp))
    EditTextBox(
        value = longitude,
        placeholderText = R.string.create_qr_longitude_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.GeolocationLongitudeChanged(longitude = it))
        }
    )
}

@Composable
private fun LinkDataEntry(
    url: String,
    onValueChange: (QrTypeFieldChange) -> Unit,
) {
    EditTextBox(
        value = url,
        placeholderText = R.string.create_qr_link_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.LinkChanged(url = it))
        },
    )
}


@Composable
private fun PhoneNumberDataEntry(
    phoneNumber: String,
    onValueChange: (QrTypeFieldChange) -> Unit,
) {
    EditTextBox(
        value = phoneNumber,
        placeholderText = R.string.create_qr_phone_number_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.PhoneNumberChanged(phoneNumber = it))
        },
    )
}

@Composable
private fun WifiDataEntry(
    ssid: String,
    password: String,
    encryptionType: String,
    onValueChange: (QrTypeFieldChange) -> Unit,
) {
    EditTextBox(
        value = ssid,
        placeholderText = R.string.create_qr_ssid_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.WifiSsidChanged(ssid = it))
        }
    )
    Spacer(Modifier.size(8.dp))
    EditTextBox(
        value = password,
        placeholderText = R.string.create_qr_password_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.WifiPasswordChanged(password = it))
        }
    )
    Spacer(Modifier.size(8.dp))
    EditTextBox(
        value = encryptionType,
        placeholderText = R.string.create_qr_encryption_type_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.WifiEncryptionTypeChanged(encryptionType = it))
        }
    )
}

@Composable
private fun TextDataEntry(
    text: String,
    onValueChange: (QrTypeFieldChange) -> Unit,
) {
    EditTextBox(
        value = text,
        placeholderText = R.string.create_qr_text_placeholder,
        onValueChange = {
            onValueChange(QrTypeFieldChange.TextChanged(text = it))
        },
    )
}

@Composable
private fun EditTextBox(
    value: String,
    placeholderText: Int,
    onValueChange: (String) -> Unit,
){
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(
                text = stringResource(placeholderText),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.extendedColours.onSurfaceAlt
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun GenerateQrButton(
    enabled: Boolean,
    onGenerateQrCode: () -> Unit
){
    Button(
        onClick = onGenerateQrCode,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.extendedColours.onSurfaceDisabled
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.generate_qr),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.extendedColours.onSurfaceDisabled
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun WifiPreview(){
    QRCraftTheme {
        QrDataEntryView(
            screenTitle = R.string.create_qr_wifi,
            qrTypeDataEntry = QrData.Wifi(
                ssid = "",
                password = "",
                encryptionType = ""
            ),
            onValueChange = {},
            onGenerateQrCode = {},
            onNavigateBack = {})
    }
}

@Composable
@Preview(showSystemUi = true)
fun ContactPreview(){
    QRCraftTheme {
        QrDataEntryView(
            screenTitle = R.string.create_qr_contact,
            qrTypeDataEntry = QrData.Contact(
                name = "",
                phoneNumber = "",
                email = ""
            ),
            onValueChange = {},
            onGenerateQrCode = {},
            onNavigateBack = {})
    }
}

@Composable
@Preview(showSystemUi = true)
fun GeolocationPreview(){
    QRCraftTheme {
        QrDataEntryView(
            screenTitle = R.string.create_qr_geolocation,
            qrTypeDataEntry = QrData.Geolocation(
                longitude = "",
                latitude = "",
            ),
            onValueChange = {},
            onGenerateQrCode = {},
            onNavigateBack = {})
    }
}

@Composable
@Preview(showSystemUi = true)
fun TextPreview(){
    QRCraftTheme {
        QrDataEntryView(
            screenTitle = R.string.create_qr_text,
            qrTypeDataEntry = QrData.Text(
                text = "",
            ),
            onValueChange = {},
            onGenerateQrCode = {},
            onNavigateBack = {})
    }
}

@Composable
@Preview(showSystemUi = true)
fun LinkPreview(){
    QRCraftTheme {
        QrDataEntryView(
            screenTitle = R.string.create_qr_link,
            qrTypeDataEntry = QrData.Link(
                url = "",
            ),
            onValueChange = {},
            onGenerateQrCode = {},
            onNavigateBack = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PhoneNumberPreview(){
    QRCraftTheme {
        QrDataEntryView(
            screenTitle = R.string.create_qr_phone_number,
            qrTypeDataEntry = QrData.PhoneNumber(
                phoneNumber = "",
            ),
            onValueChange = {},
            onGenerateQrCode = {},
            onNavigateBack = {}
        )
    }
}





