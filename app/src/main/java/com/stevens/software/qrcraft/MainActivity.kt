package com.stevens.software.qrcraft

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stevens.software.qrcraft.di.appModule
import com.stevens.software.qrcraft.ui.theme.QRCraftTheme
import com.stevens.software.qrcraft.ui.toolkit.BottomNavigationBar
import org.koin.android.ext.koin.androidContext
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
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route
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
                                currentRoute = currentDestination,
                                onNavigateToAddQrCode = { navController.navigate(AddQrChooseType) },
                                onNavigateToScanQrCode = { navController.navigate(QrCamera) },
                                onNavigateToQrHistory = { navController.navigate(QrHistory) }
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
