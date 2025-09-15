package com.stevens.software.core.di

import com.stevens.software.qrcraft.db.QrCodeRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val coreModule = module {
    factoryOf(::QrCodeRepository)
}