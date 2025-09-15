package com.stevens.software.analyzer.di

import com.stevens.software.analyzer.QrCodeAnalyzer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val analyzerModule = module {
    factoryOf(::QrCodeAnalyzer)
}