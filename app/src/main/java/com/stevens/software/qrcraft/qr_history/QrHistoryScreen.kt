package com.stevens.software.qrcraft.qr_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.toolkit.TopNavBar
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.ui.theme.extendedColours

@Composable
fun QrHistoryScreen() {
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
            EmptyState()
        }

    }
}

@Composable
private fun EmptyState(){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.scan_history_empty),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.extendedColours.onSurfaceAlt
        )
    }
}

@Composable
@Preview
fun ScanHistoryScreenPreview(){
    QRCraftTheme {
        QrHistoryScreen()
    }
}
