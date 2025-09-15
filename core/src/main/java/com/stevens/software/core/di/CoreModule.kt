package com.stevens.software.core.di

import com.stevens.software.core.QrCodeRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreModule = module {
    factoryOf(::QrCodeRepository)
}