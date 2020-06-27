//package com.sg.core.di
////
////import com.sg.core.data.local.AppDatabase
////import org.koin.dsl.module
////
////val roomModule = module {
////    single { AppDatabase.getInstance(get()) }
////    single { get<AppDatabase>().userDao() }
////    single { get<AppDatabase>().messageDao()}
////}