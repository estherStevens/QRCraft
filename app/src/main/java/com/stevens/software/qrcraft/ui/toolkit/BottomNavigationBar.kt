package com.stevens.software.qrcraft.ui.toolkit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.theme.extendedColours

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    onNavigateToAddQrCode: () -> Unit, 
    onNavigateToScanQrCode: () -> Unit
) {
    Box(
        modifier = modifier
            .width(168.dp)
            .height(64.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    color = MaterialTheme.extendedColours.surfaceHigher,
                    shape = CircleShape
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.history_icon),
                    contentDescription = stringResource(R.string.qr_history_content_description),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.width(60.dp))
            IconButton(
                onClick = onNavigateToAddQrCode,
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_qr_icon),
                    contentDescription = stringResource(R.string.add_qr_content_description),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Box(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.Center)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onNavigateToScanQrCode
            ) {
                Icon(
                    painter = painterResource(R.drawable.qr_scan_icon),
                    contentDescription = stringResource(R.string.scan_qr_content_description),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview()
@Composable
fun BottomNavigationBarPreview() {
    QRCraftTheme {
        BottomNavigationBar(
            onNavigateToAddQrCode = {},
            onNavigateToScanQrCode = {}
        )
    }
}
