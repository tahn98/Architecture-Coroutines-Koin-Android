package com.sg.core.di

import com.sg.core.api.ApiService
import com.sg.core.data.local.MessageDao
import com.sg.core.data.local.UserDao
import com.sg.core.repository.AuthRepository
import com.sg.core.repository.impl.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * The binding for TasksRepository is on its own module so that we can replace it easily in tests.
 */

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModuleHilt {

//
//    @Singleton
//    @RepositoryModule
//    @Provides
//    fun provideAuthRepository(
//        api: ApiService,
//        useDao: UserDao,
//        messageDao: MessageDao
//    ): AuthRepository {
//        return AuthRepositoryImpl(api, useDao, messageDao)
//    }

    @AnoRepo
    @ActivityScoped
    @Binds
    abstract fun bindAuthRepo(impl: AuthRepositoryImpl): AuthRepository

}