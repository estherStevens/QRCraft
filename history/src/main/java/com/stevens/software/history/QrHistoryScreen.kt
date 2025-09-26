package com.stevens.software.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.uitoolkit.theme.QRCraftTheme
import com.stevens.software.uitoolkit.theme.extendedColours
import com.stevens.software.uitoolkit.toolkit.TopNavBar
import com.stevens.software.uitoolkit.R
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun QrHistoryScreen(
    viewModel: QrHistoryViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    QrHistoryView(
        scannedQrs = uiState.value.scannedQrs,
        generatedQrs = uiState.value.generatedQrs
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QrHistoryView(
    scannedQrs: List<HistoricQrCode>,
    generatedQrs: List<HistoricQrCode>
) {
    Scaffold(
        topBar = {
            TopNavBar(
                title = stringResource(R.string.scan_history_title),
                backgroundColor = MaterialTheme.colorScheme.surface,
                titleColour = MaterialTheme.colorScheme.onSurface
            )
        }
    ) { contentPadding ->
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val tabs = listOf(
            stringResource(R.string.scan_history_scanned),
            stringResource(R.string.scan_history_generated)
        ) //todo - move to uistate


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stickyHeader {
                    Tabs(
                        tabs = tabs,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = {
                            selectedTabIndex = it
                        }
                    )
                    Spacer(Modifier.size(12.dp))
                }
                when(selectedTabIndex) {
                    0 -> historicQrs(scannedQrs)
                    1 -> historicQrs(generatedQrs)
                }

            }

        }

    }
}

private fun LazyListScope.historicQrs(historicQrs: List<HistoricQrCode>){
    when(historicQrs.isEmpty()) {
        true ->  item { EmptyState() }
        false -> this.items(historicQrs) { QrHistoryRow(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Tabs(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(selectedTabIndex)
                    .padding(horizontal = 18.dp),
                color = MaterialTheme.colorScheme.onSurface,
                height = 2.dp,
                width = Dp.Unspecified
            )
        }
    ) {
        tabs.forEachIndexed { index, tabTitle ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                          },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = MaterialTheme.extendedColours.onSurfaceAlt,
                text = {
                    Text(
                        text = tabTitle,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()

                    )
                }
            )
        }
    }
}

@Composable
private fun QrHistoryRow(qrHistoricQrCode: HistoricQrCode) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.extendedColours.surfaceHigher,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(modifier = Modifier
            .padding(12.dp)
        ) {
            Icon(
                painter = qrHistoricQrCode.toQrTypeImage(),
                tint = Color.Unspecified,
                contentDescription = null
            )
            Spacer(Modifier.size(12.dp))
            Column {
                Text(
                    text = qrHistoricQrCode.toQrTypeTitle(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = qrHistoricQrCode.toQrDataText(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.extendedColours.onSurfaceAlt,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = qrHistoricQrCode.createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.extendedColours.onSurfaceDisabled
                )
            }
        }
    }
}



@Composable
private fun HistoricQrCode.toQrTypeImage() =
    when (this) {
        is HistoricQrCode.ContactDetails -> painterResource(R.drawable.contact_details_qr_type_icon)
        is HistoricQrCode.Geolocation -> painterResource(R.drawable.geolocation_qr_type_icon)
        is HistoricQrCode.PhoneNumber -> painterResource(R.drawable.phone_number_qr_type_icon)
        is HistoricQrCode.PlainText -> painterResource(R.drawable.text_qr_type_icon)
        is HistoricQrCode.Url -> painterResource(R.drawable.link_qr_type_icon)
        is HistoricQrCode.Wifi -> painterResource(R.drawable.wifi_qr_type_icon)
    }

@Composable
private fun HistoricQrCode.toQrTypeTitle() =
    when (this) {
        is HistoricQrCode.ContactDetails -> stringResource(R.string.qr_type_contact_details)
        is HistoricQrCode.Geolocation -> stringResource(R.string.qr_type_geolocation)
        is HistoricQrCode.PhoneNumber -> stringResource(R.string.qr_type_phone_number)
        is HistoricQrCode.PlainText -> stringResource(R.string.qr_type_text)
        is HistoricQrCode.Url -> stringResource(R.string.qr_type_link)
        is HistoricQrCode.Wifi -> stringResource(R.string.qr_type_wifi)
    }


@Composable
private fun HistoricQrCode.toQrDataText() =
    when (this) {
        is HistoricQrCode.ContactDetails -> buildString {
            appendLine(name)
            appendLine(email)
            appendLine(tel)
        }
        is HistoricQrCode.Geolocation -> "$longitude, $latitude"
        is HistoricQrCode.PhoneNumber -> phoneNumber
        is HistoricQrCode.PlainText -> text
        is HistoricQrCode.Url -> link
        is HistoricQrCode.Wifi -> buildString {
            appendLine(stringResource(R.string.qr_type_wifi_ssid, ssid))
            appendLine(stringResource(R.string.qr_type_wifi_password, password))
            appendLine(stringResource(R.string.qr_type_wifi_encryption_type, encryptionType))
        }
    }


@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.scan_history_empty),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.extendedColours.onSurfaceAlt
        )
    }
}

@Composable
@Preview
fun ScanHistoryScreenPreview() {
    QRCraftTheme {
        QrHistoryView(
            listOf(
                HistoricQrCode.Wifi(
                    ssid = "Esther",
                    password = "esther@gmail.com",
                    encryptionType = "2",
                    createdAt = "",
                    userGenerated = true
                ),
                HistoricQrCode.Wifi(
                    ssid = "Esther",
                    password = "esther@gmail.com",
                    encryptionType = "2",
                    createdAt = "",
                    userGenerated = true
                )
            ),
            emptyList(),
        )


    }
}
