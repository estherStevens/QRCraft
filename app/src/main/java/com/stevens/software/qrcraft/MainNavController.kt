package com.stevens.software.qrcraft

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.stevens.software.qrcraft.generate_qr.data_entry.QrDataEntryScreen
import com.stevens.software.qrcraft.generate_qr.select_type.QrType
import com.stevens.software.qrcraft.generate_qr.select_type.SelectQrCodeTypeScreen
import com.stevens.software.qrcraft.qr_camera.CameraScreen
import com.stevens.software.qrcraft.qr_camera.data.QrCodeData
import com.stevens.software.qrcraft.qr_result.QrResultScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
sealed interface AppRoute

@Serializable
object QrCamera: AppRoute

@Serializable
data class ScanResult(val qrCodeBitmapFilePath: String, val qrData: String): AppRoute

@Serializable
object AddQrChooseType: AppRoute

@Serializable
data class QrDataEntry(val qrType: QrType): AppRoute

@Composable
fun MainNavController(
    navController: NavHostController
) {
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
        composable<AddQrChooseType> {
            SelectQrCodeTypeScreen(
                viewModel = koinViewModel(),
                onNavigateToDataEntry = { navController.navigate(QrDataEntry(it)) }
            )
        }
        composable<QrDataEntry> { backStackEntry ->
            val routeArgs = backStackEntry.toRoute<QrDataEntry>()
            QrDataEntryScreen(
                viewModel = koinViewModel(
                    parameters = {
                        parametersOf(
                            routeArgs.qrType
                        )
                    }
                ),
//                onNavigateToDataEntry = { navController.navigate(QrDataEntry(it)) }
            )
        }
    }
}
