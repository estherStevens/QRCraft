package com.stevens.software.qrcraft.qr_camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Size
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.stevens.software.qrcraft.ui.theme.extendedColours
import kotlinx.coroutines.launch
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.core.ImplementationMode
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stevens.software.qrcraft.R
import com.stevens.software.qrcraft.ui.toolkit.CustomSnackBar
import com.stevens.software.qrcraft.ui.toolkit.QRScannerOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    onNavigateToScanResult: (String, String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
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
    var launchCamera by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera = true
            viewModel.showPermissionGrantedSnackBar()
        }
    }

    val snackBarMessage = stringResource(R.string.camera_permission_granted_snackbar)
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
        } else {
            launchCamera = true
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
        Box(modifier = Modifier.padding(contentPadding)) {
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
            if (launchCamera) {
                QRScannerView(
                    isLoading = uiState.value.isLoading,
                    onQrScanned = { qrCodeBitmapFilePath, rawData ->
                        onNavigateToScanResult(qrCodeBitmapFilePath, rawData)
                    },
                    onQrDetected = {
                        viewModel.onQrCodeDetected()
                    }
                )
            }
        }
    }
}

@Composable
fun QRScannerView(
    isLoading: Boolean,
    onQrScanned: (String, String) -> Unit,
    onQrDetected: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 1280))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }
    var surfaceRequest by remember { mutableStateOf<SurfaceRequest?>(null) }

    LaunchedEffect(Unit) {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(context),
            QrCodeAnalyzer(
                context = context,
                onQrCodeDetected = onQrDetected,
                onQrCodeScanned = { qrCodeBitmapFilePath, rawData ->
                    onQrScanned(qrCodeBitmapFilePath, rawData)
                }
        )
        )
    }

    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        preview.setSurfaceProvider { request ->
            surfaceRequest = request
        }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )
    }


    Box {
        surfaceRequest?.let {
            CameraXViewfinder(
                surfaceRequest = it,
                modifier = Modifier.fillMaxSize(),
                implementationMode = ImplementationMode.EXTERNAL,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
        }

        QRScannerOverlay()

        if(isLoading) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(32.dp),
                    color = MaterialTheme.extendedColours.onOverlay

                )
                Spacer(Modifier.size(12.dp))
                Text(
                    stringResource(R.string.qr_detected_loading),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.extendedColours.onOverlay
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
) {
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
private fun DialogTitle() {
    Text(
        text = stringResource(R.string.camera_permission_dialog_title),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun DialogSubtitle() {
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
) {
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
) {
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

