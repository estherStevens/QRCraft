package com.stevens.software.scanner.di

//import com.stevens.software.scanner.QrCodeAnalyzer
import com.stevens.software.scanner.ui.CameraViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val scannerModule = module {
    viewModelOf(::CameraViewModel)
//    factoryOf(::QrCodeAnalyzer)
}