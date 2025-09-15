package com.stevens.software.analyzer.di

import com.stevens.software.result.QrCodeAnalyzer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val analyzerModule = module {
    factoryOf(::QrCodeAnalyzer)
}