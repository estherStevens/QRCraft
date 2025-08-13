package com.stevens.software.qrcraft

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.theme.extendedColours
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val permission = Manifest.permission.CAMERA
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, permission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var showDialog by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.showPermissionGrantedSnackBar()
        }
    }


    val snackBarMessage =  stringResource(R.string.camera_permission_granted_snackbar)
    LaunchedEffect(Unit) {
        viewModel.snackBar.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = snackBarMessage
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        if (hasPermission.not()) {
            showDialog = true
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    CustomSnackBar(snackbarData)
                }
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)){
            if (showDialog) {
                CameraPermissionsDialog(
                    onDismissDialog = {
                        showDialog = false
                    },
                    onCloseApp = {
                        activity?.finish()
                    },
                    launchPermissionDialog = {
                        showDialog = false
                        permissionLauncher.launch(permission)
                    }
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CameraPermissionsDialog(
    onDismissDialog: () -> Unit,
    onCloseApp: () -> Unit,
    launchPermissionDialog: () -> Unit
){
    BasicAlertDialog(
        onDismissRequest = onDismissDialog
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)

            ) {
                DialogTitle()
                Spacer(Modifier.size(10.dp))
                DialogSubtitle()
                Spacer(Modifier.size(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CloseAppDialogButton(onCloseApp = onCloseApp)
                    Spacer(Modifier.size(8.dp))
                    GrantPermissionDialogButton(launchPermissionDialog = launchPermissionDialog)
                }
            }
        }
    }
}

@Composable
private fun DialogTitle(){
    Text(
        text = stringResource(R.string.camera_permission_dialog_title),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun DialogSubtitle(){
    Text(
        text = stringResource(R.string.camera_permission_dialog_content),
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun CloseAppDialogButton(
    onCloseApp: () -> Unit
){
    Button(
        onClick = onCloseApp,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.extendedColours.surfaceHigher
        ),
        shape = CircleShape
    ) {
        Text(
            text = stringResource(R.string.camera_permission_dialog_cancel),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun GrantPermissionDialogButton(
    launchPermissionDialog: () -> Unit
){
    Button(
        onClick = launchPermissionDialog,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.extendedColours.surfaceHigher
        ),
        shape = CircleShape
    ) {
        Text(
            text = stringResource(R.string.camera_permission_dialog_confirm),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


@Composable
private fun CustomSnackBar(
    snackbarData: SnackbarData
){
    Box(
        modifier = Modifier.background(
            color = MaterialTheme.extendedColours.success,
            shape = RoundedCornerShape(6.dp)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.tick_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = snackbarData.visuals.message,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

    }
}
