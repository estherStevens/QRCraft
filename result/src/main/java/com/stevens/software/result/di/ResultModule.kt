package com.stevens.software.result.di

import com.stevens.software.result.ui.PreviewQrViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val resultModule = module {
    viewModelOf(::PreviewQrViewModel)
}