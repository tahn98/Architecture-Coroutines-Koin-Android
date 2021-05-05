package com.sg.core.di

import com.sg.core.repository.*
import com.sg.core.repository.impl.*
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }
}