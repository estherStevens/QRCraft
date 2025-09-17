package com.stevens.software.history.di

import com.stevens.software.history.QrHistoryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val historyModule = module {
    viewModelOf(::QrHistoryViewModel)
}