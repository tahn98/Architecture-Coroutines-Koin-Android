package com.sg.base.di

import com.sg.data.repository.*
import com.sg.domain.repository.AuthRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get(), get()) }
}