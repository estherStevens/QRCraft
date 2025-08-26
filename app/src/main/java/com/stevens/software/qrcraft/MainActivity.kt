package com.stevens.software.qrcraft

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.stevens.software.qrcraft.di.appModule
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.toolkit.BottomNavigationBar
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.GlobalContext.startKoin

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }
        setContent {
            QRCraftTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(
                                    WindowInsets.navigationBars
                                )
                                .padding(bottom = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BottomNavigationBar(
                                onNavigateToAddQrCode = { navController.navigate(AddQrChooseType) },
                                onNavigateToScanQrCode = { navController.navigate(QrCamera) }
                            )
                        }
                    }
                ) { _ ->
                    MainNavController(navController)
                }
            }
        }
    }
}
