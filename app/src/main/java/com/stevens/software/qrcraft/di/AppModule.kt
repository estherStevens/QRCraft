package com.stevens.software.qrcraft.di

import com.stevens.software.qrcraft.qr_camera.CameraViewModel
import com.stevens.software.qrcraft.qr_result.data.QrDataParser
import com.stevens.software.qrcraft.qr_result.data.QrDataParserImpl
import com.stevens.software.qrcraft.qr_result.QrResultViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CameraViewModel)
    viewModelOf(::QrResultViewModel)
    factoryOf(::QrDataParserImpl) bind QrDataParser::class
}