package com.stevens.software.history

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stevens.software.analyzer.QrCodeData
import com.stevens.software.uitoolkit.theme.QRCraftTheme
import com.stevens.software.uitoolkit.theme.extendedColours
import com.stevens.software.uitoolkit.toolkit.TopNavBar
import com.stevens.software.uitoolkit.R
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun QrHistoryScreen(
    viewModel: QrHistoryViewModel,
    onNavigateToPreview: (Int) -> Unit
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    QrHistoryView(
        scannedQrs = uiState.value.scannedQrs,
        generatedQrs = uiState.value.generatedQrs,
        onDeleteQr = {
            viewModel.deleteQrCode(it)
        },
        onShare = {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, it)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        },
        onNavigateToPreview = onNavigateToPreview
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QrHistoryView(
    scannedQrs: List<HistoricQrCode>,
    generatedQrs: List<HistoricQrCode>,
    onDeleteQr: (Int) -> Unit,
    onShare: (String) -> Unit,
    onNavigateToPreview: (Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedQrCode by remember { mutableStateOf<HistoricQrCode?>(null) }

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
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
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
                    0 -> historicQrs(
                        historicQrs = scannedQrs,
                        onShowBottomSheet = {
                            selectedQrCode = it
                            showBottomSheet = true
                        },
                        onNavigateToPreview = { id ->
                            onNavigateToPreview(id)
                        }
                    )
                    1 -> historicQrs(
                        historicQrs = generatedQrs,
                        onShowBottomSheet = {
                            selectedQrCode = it
                            showBottomSheet = true
                        },
                        onNavigateToPreview = onNavigateToPreview
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .zIndex(1f)
            )
        }

        if (showBottomSheet) {
            selectedQrCode?.let {
                BottomSheet(
                    sheetState = sheetState,
                    onDismiss = {
                        showBottomSheet = false
                                },
                    onShare = {
                        showBottomSheet = false
                        val qrData = extractDataAndConvertToString(it)
                        onShare(qrData)
                    },
                    onDelete = {
                        showBottomSheet = false
                        onDeleteQr(it.id)
                    }
                )
            }
        }
    }
}

private fun LazyListScope.historicQrs(
    historicQrs: List<HistoricQrCode>,
    onShowBottomSheet: (HistoricQrCode) -> Unit,
    onNavigateToPreview: (Int) -> Unit
){
    when(historicQrs.isEmpty()) {
        true ->  item { EmptyState() }
        false -> this.items(historicQrs) {
            QrHistoryRow(
                qrHistoricQrCode = it,
                onShowBottomSheet = { historicQrCode ->
                    onShowBottomSheet(historicQrCode)
                },
                onNavigateToPreview = { historicQrCodeId ->
                    onNavigateToPreview(historicQrCodeId)
                }
            )
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QrHistoryRow(
    qrHistoricQrCode: HistoricQrCode,
    onShowBottomSheet: (HistoricQrCode) -> Unit,
    onNavigateToPreview: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.extendedColours.surfaceHigher,
                shape = RoundedCornerShape(16.dp)
            )
            .combinedClickable(
                onClick = {
                    onNavigateToPreview(qrHistoricQrCode.id)
                },
                onLongClick = {
                    onShowBottomSheet(qrHistoricQrCode)
                }
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


private fun extractDataAndConvertToString(historicQrCode: HistoricQrCode): String =
    when(historicQrCode){

        is HistoricQrCode.ContactDetails -> buildString{
            appendLine(historicQrCode.name)
            appendLine(historicQrCode.tel)
            appendLine(historicQrCode.email)
        }
        is HistoricQrCode.Geolocation ->  buildString {
            append(historicQrCode.latitude)
            append(", ")
            append(historicQrCode.longitude)
        }
        is HistoricQrCode.PhoneNumber ->  historicQrCode.phoneNumber
        is HistoricQrCode.PlainText ->  historicQrCode.text
        is HistoricQrCode.Url ->  historicQrCode.link
        is HistoricQrCode.Wifi -> buildString {
            appendLine(historicQrCode.ssid)
            appendLine(historicQrCode.password)
            appendLine(historicQrCode.encryptionType)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
){
    ModalBottomSheet(
        containerColor = MaterialTheme.extendedColours.surfaceHigher,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        content = {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                BottomSheetButton(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.share_icon),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.bottom_sheet_share),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = onShare
                )
                BottomSheetButton(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.bin_icon),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.bottom_sheet_delete),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = onDelete
                )
            }
        }
    )
}

@Composable
private fun BottomSheetButton(
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
){
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            text()
            Spacer(Modifier.size(8.dp))
            icon()
        }
    }
}

@Composable
@Preview
fun ScanHistoryScreenPreview() {
    QRCraftTheme {
        QrHistoryView(
            listOf(
                HistoricQrCode.Wifi(
                    id = 1,
                    ssid = "Esther",
                    password = "esther@gmail.com",
                    encryptionType = "2",
                    createdAt = "",
                    userGenerated = true
                ),
                HistoricQrCode.Wifi(
                    id = 2,
                    ssid = "Esther",
                    password = "esther@gmail.com",
                    encryptionType = "2",
                    createdAt = "",
                    userGenerated = true
                )
            ),
            emptyList(),
            onDeleteQr = {},
            onShare = {},
            onNavigateToPreview = {}
        )
    }
}
