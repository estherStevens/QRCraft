package com.stevens.software.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.uitoolkit.theme.QRCraftTheme
import com.stevens.software.uitoolkit.theme.extendedColours
import com.stevens.software.uitoolkit.toolkit.TopNavBar
import com.stevens.software.uitoolkit.R

@Composable
fun QrHistoryScreen(
    viewModel: QrHistoryViewModel
){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    QrHistoryView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QrHistoryView() {
    Scaffold(
        topBar = {
            TopNavBar(
                title = stringResource(R.string.scan_history_title),
                backgroundColor = MaterialTheme.colorScheme.surface,
                titleColour = MaterialTheme.colorScheme.onSurface
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {

            LazyColumn {
                item {
                    Tabs()
                }
                item {
                    Spacer(Modifier.size(12.dp))
                    QrHistoryRow()
                }
            }

            EmptyState()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Tabs(){
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.scan_history_scanned),
        stringResource(R.string.scan_history_generated)
    ) //todo - move to uistate

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
                onClick = { selectedTabIndex = index },
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
private fun QrHistoryRow() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.extendedColours.surfaceHigher,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Icon(
                painter = painterResource(R.drawable.link_qr_type_icon),
                tint = Color.Unspecified,
                contentDescription = null
            )
            Spacer(Modifier.size(12.dp))
            Column {
                Text(
                    text = "Link",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.size(4.dp))
                Text(
                    text = "https://www.google.com/maps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.extendedColours.onSurfaceAlt
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "24 Jun 2025, 14:36",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.extendedColours.onSurfaceDisabled
                )
            }




        }

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
        QrHistoryView()
    }
}
