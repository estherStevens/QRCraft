package com.stevens.software.qrcraft

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.stevens.software.generator.QrType
import com.stevens.software.generator.SelectQrCodeTypeScreen
import com.stevens.software.generator.ui.QrDataEntryScreen
import com.stevens.software.history.QrHistoryScreen
import com.stevens.software.qrcraft.scanned_qr_result.QrResultScreen
import com.stevens.software.result.ui.PreviewQrScreen
import com.stevens.software.scanner.ui.CameraScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Serializable
sealed interface AppRoute

@Serializable
object QrCamera: AppRoute

@Serializable
data class ScanResult(val qrCodeId: Long): AppRoute

@Serializable
object AddQrChooseType: AppRoute

@Serializable
data class QrDataEntry(val qrType: QrType): AppRoute

@Serializable
data class PreviewQr(val qrCodeId: Long): AppRoute

@Serializable
object QrHistory: AppRoute


@Composable
fun MainNavController(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = QrCamera){
        composable<QrCamera>{
            CameraScreen(
                viewModel = koinViewModel(),
                onNavigateToScanResult = { qrCodeId->
                    navController.navigate(
                        ScanResult(qrCodeId)
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
                            routeArgs.qrCodeId
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
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPreviewQr = { qrCodeBitmapFilePath ->
                    navController.navigate(PreviewQr(qrCodeBitmapFilePath))
                }
            )
        }
        composable<PreviewQr> { backStackEntry ->
            val routeArgs = backStackEntry.toRoute<PreviewQr>()
            PreviewQrScreen(
                viewModel = koinViewModel(
                    parameters = {
                        parametersOf(
                            routeArgs.qrCodeId
                        )
                    }
                ),
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<QrHistory> {
            QrHistoryScreen(

            )
        }
    }
}
