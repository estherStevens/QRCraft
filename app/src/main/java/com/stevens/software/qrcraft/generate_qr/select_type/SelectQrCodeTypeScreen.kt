package com.stevens.software.qrcraft.generate_qr.select_type

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.theme.extendedColours

@Composable
fun SelectQrCodeTypeScreen(
    viewModel: SelectQrCodeTypeViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    SelectQrCodeTypeView(
        qrCodeTypes = uiState.value.qrTypes
    )
}

@Composable
private fun SelectQrCodeTypeView(
    qrCodeTypes: List<QrType>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .safeDrawingPadding()
    ) {
        Spacer(Modifier.size(10.dp))
        Text(
            text = stringResource(R.string.create_qr_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(qrCodeTypes) {
                QrTypeBox(
                    it.drawable, it.text
                )
            }

        }
    }
}
@Composable
private fun QrTypeBox(
    drawable: Int,
    text: Int
){
    Box(
        modifier = Modifier
            .height(108.dp)
            .background(
                color = MaterialTheme.extendedColours.surfaceHigher,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(drawable),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Spacer(Modifier.size(12.dp))
            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun SelectQrCodeTypeScreenPreview(){
    QRCraftTheme {
        SelectQrCodeTypeView(
            buildList {
                add(QrType(drawable = R.drawable.text_qr_type_icon, text = R.string.qr_type_text))
                add(QrType(drawable = R.drawable.link_qr_type_icon, text = R.string.qr_type_link))
                add(QrType(drawable = R.drawable.contact_details_qr_type_icon, text = R.string.qr_type_contact_details))
                add(QrType(drawable = R.drawable.phone_number_qr_type_icon, text = R.string.qr_type_phone_number))
                add(QrType(drawable = R.drawable.geolocation_qr_type_icon, text = R.string.qr_type_geolocation))
                add(QrType(drawable = R.drawable.wifi_qr_type_icon, text = R.string.qr_type_wifi))
            }
        )
    }
}