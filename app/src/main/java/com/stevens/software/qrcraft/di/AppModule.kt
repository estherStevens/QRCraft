package com.stevens.software.qrcraft.di

import com.stevens.software.qrcraft.qr_camera.CameraViewModel
import com.stevens.software.qrcraft.qr_result.QrResultViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CameraViewModel)
    viewModelOf(::QrResultViewModel)
}