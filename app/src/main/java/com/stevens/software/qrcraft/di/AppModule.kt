package com.stevens.software.qrcraft.di

import com.stevens.software.qrcraft.generate_qr.data_entry.ui.QrDataEntryViewModel
import com.stevens.software.qrcraft.generate_qr.data_entry.data.QrGeneratorRepository
import com.stevens.software.qrcraft.generate_qr.data_entry.data.QrGeneratorRepositoryImpl
import com.stevens.software.qrcraft.generate_qr.preview_qr.PreviewQrViewModel
import com.stevens.software.qrcraft.generate_qr.select_type.SelectQrCodeTypeViewModel
import com.stevens.software.qrcraft.qr_camera.BitmapAnalyzer
import com.stevens.software.qrcraft.qr_camera.ui.CameraViewModel
import com.stevens.software.qrcraft.scanned_qr_result.QrResultViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CameraViewModel)
    viewModelOf(::QrResultViewModel)
    viewModelOf(::SelectQrCodeTypeViewModel)
    viewModelOf(::QrDataEntryViewModel)
    viewModelOf(::PreviewQrViewModel)
    factoryOf(::QrGeneratorRepositoryImpl) bind QrGeneratorRepository::class
    factoryOf(::BitmapAnalyzer)
}