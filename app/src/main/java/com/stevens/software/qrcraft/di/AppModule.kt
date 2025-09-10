package com.stevens.software.qrcraft.di

import com.stevens.software.qrcraft.generate_qr.data_entry.QrDataEntryViewModel
import com.stevens.software.qrcraft.generate_qr.data_entry.QrGeneratorRepository
import com.stevens.software.qrcraft.generate_qr.qr_result.PreviewQrViewModel
import com.stevens.software.qrcraft.generate_qr.select_type.SelectQrCodeTypeViewModel
import com.stevens.software.qrcraft.qr_camera.BitmapAnalyzer
import com.stevens.software.qrcraft.qr_camera.CameraViewModel
import com.stevens.software.qrcraft.scanned_qr_result.data.QrDataParser
import com.stevens.software.qrcraft.scanned_qr_result.data.QrDataParserImpl
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
    factoryOf(::QrDataParserImpl) bind QrDataParser::class
    factoryOf(::QrGeneratorRepository)
    factoryOf(::BitmapAnalyzer)
}