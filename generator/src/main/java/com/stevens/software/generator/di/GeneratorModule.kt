package com.stevens.software.generator.di

import com.stevens.software.generator.SelectQrCodeTypeViewModel
import com.stevens.software.generator.data.QrGeneratorRepository
import com.stevens.software.generator.data.QrGeneratorRepositoryImpl
import com.stevens.software.generator.ui.QrDataEntryViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val generatorModule = module {
    viewModelOf(::SelectQrCodeTypeViewModel)
    viewModelOf(::QrDataEntryViewModel)
    factoryOf(::QrGeneratorRepositoryImpl) bind QrGeneratorRepository::class

}