package com.stevens.software.qrcraft

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.stevens.software.qrcraft.qr_camera.CameraScreen
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.qr_result.QrResultScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object QrCamera

@Serializable
data class ScanResult(val qrCodeBitmapFilePath: String, val qrData: String)

@Composable
fun MainNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = QrCamera){
        composable<QrCamera>{
            CameraScreen(
                viewModel = koinViewModel(),
                onNavigateToScanResult = { qrCodeBitmapFilePath, qrData ->
                    val qrJsonString = Json.encodeToString<QrCodeData?>(qrData)
                    navController.navigate(
                        ScanResult(qrCodeBitmapFilePath, qrJsonString)
                    )
                }
            )
        }
        composable<ScanResult>{ backStackEntry ->
            val routeArgs = backStackEntry.toRoute<ScanResult>()
            QrResultScreen(
                viewModel = koinViewModel(
                    parameters = {
                        parametersOf(
                            routeArgs.qrData,
                            routeArgs.qrCodeBitmapFilePath
                        )
                    }
                ),
                onNavigateBack = {
                    navController.popBackStack(
                        route = QrCamera,
                        inclusive = false
                    )
                }
            )
        }
    }
}
