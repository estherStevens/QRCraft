package com.stevens.software.qrcraft.qr_result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.theme.extendedColours

@Composable
fun QrResultScreen(
    viewModel: QrResultViewModel
) {
    QrResultView()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrResultView(){
    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.onSurface),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.scan_result),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.extendedColours.onOverlay,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_icon),
                            tint = MaterialTheme.extendedColours.onOverlay,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.onSurface)
        ){

        }
    }
}

@Preview
@Composable
fun Preview(){
    QRCraftTheme {
        QrResultView()
    }
}