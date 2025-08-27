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
    viewModel: QrDataEntryViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    uiState.value.qrCodeType?.let {
        QrDataEntryView(qrCodeType = it,
            value = uiState.value.value,
            onValueChange = { value ->
                viewModel.updateValue(value)
            })
    }
}

@Composable
private fun QrDataEntryView(qrCodeType: QrType,
                            value: String,
                            onValueChange: (String) -> Unit){
    Scaffold(
        topBar = {
            TopNavBar(
                title = qrCodeType.getQrTypeTitle(),
                backgroundColor = MaterialTheme.colorScheme.surface,
                iconColour = MaterialTheme.colorScheme.onSurface,
                titleColour = MaterialTheme.colorScheme.onSurface,
                onNavigateBack = {}
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
                when(qrCodeType){
                    QrType.TEXT -> {
                        TextDataEntry(
                            value = value,
                            onValueChange = onValueChange,
                        )
                    }
                    QrType.LINK -> TODO()
                    QrType.CONTACT -> TODO()
                    QrType.PHONE_NUMBER -> TODO()
                    QrType.GEOLOCATION -> TODO()
                    QrType.WIFI -> TODO()
                }
                Spacer(Modifier.size(16.dp))
                GenerateQrButton(
                    enabled = true,
                    onGenerateQrCode = {}
                )
            }


        }
    }
}

@Composable
private fun TextDataEntry(
    value: String,
    onValueChange: (String) -> Unit,){
    EditTextBox(
        value = value,
        onValueChange = onValueChange,
    )
}

@Composable
private fun EditTextBox(
    value: String,
    onValueChange: (String) -> Unit,
){
    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(
                text = stringResource(R.string.create_qr_text_placeholder),
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
private fun QrType?.getQrTypeTitle() =
    when(this){
        QrType.TEXT -> stringResource(R.string.create_qr_text)
        QrType.LINK -> stringResource(R.string.create_qr_link)
        QrType.CONTACT -> stringResource(R.string.create_qr_contact)
        QrType.PHONE_NUMBER -> stringResource(R.string.create_qr_phone_number)
        QrType.GEOLOCATION -> stringResource(R.string.create_qr_geolocation)
        QrType.WIFI -> stringResource(R.string.create_qr_wifi)
        null -> ""
    }

@Composable
@Preview(showSystemUi = true)
fun Preview(){
    QRCraftTheme {
        QrDataEntryView(QrType.TEXT, "", {})

    }
}


