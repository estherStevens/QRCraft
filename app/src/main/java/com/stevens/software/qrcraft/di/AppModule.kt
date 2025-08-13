package com.stevens.software.qrcraft.di

import com.stevens.software.qrcraft.CameraViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CameraViewModel)
}