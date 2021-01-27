package com.sg.base.di

import com.sg.data.datasource.local.AppDatabase
import org.koin.dsl.module

val roomModule = module {
    single { AppDatabase.getInstance(get()) }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().messageDao()}
}