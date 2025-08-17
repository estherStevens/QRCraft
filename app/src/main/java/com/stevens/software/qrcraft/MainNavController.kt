package com.stevens.software.qrcraft

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.stevens.software.qrcraft.qr_camera.CameraScreen
import com.stevens.software.qrcraft.qr_result.QrResultScreen
import com.stevens.software.qrcraft.qr_result.QrResultViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
object QrCamera

@Serializable
data class ScanResult(val rawQrData: String)

@Composable
fun MainNavController() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = QrCamera){
        composable<QrCamera>{
            CameraScreen(
                viewModel = koinViewModel(),
                onNavigateToScanResult = {
                    navController.navigate(
                        ScanResult(it)
                    )
                }
            )
        }
        composable<ScanResult> { navBackStackEntry ->
            val routeArgs = navBackStackEntry.toRoute<ScanResult>()
            QrResultScreen(
                viewModel = koinViewModel(
                    parameters = {
                        parametersOf(
                            routeArgs.rawQrData
                        )
                    }
                )
            )
        }
    }
}