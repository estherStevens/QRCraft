package com.stevens.software.qrcraft.generate_qr.data_entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.ui.theme.extendedColours
import com.stevens.software.qrcraft.ui.toolkit.TopNavBar

@Composable
fun QrDataEntryScreen(
    qrCodeType: String
) {
    Scaffold(
        topBar = {
            TopNavBar(
                title = stringResource(R.string.scan_result),
                backgroundColor = MaterialTheme.colorScheme.onSurface,
                iconColour = MaterialTheme.extendedColours.onOverlay,
                onNavigateBack = {}
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)){

        }
    }
}