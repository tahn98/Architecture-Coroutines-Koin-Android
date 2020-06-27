package com.sg.core.di

import com.sg.core.api.ApiService
import com.sg.core.data.local.MessageDao
import com.sg.core.data.local.UserDao
import com.sg.core.repository.AuthRepository
import com.sg.core.repository.impl.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

/**
 * The binding for TasksRepository is on its own module so that we can replace it easily in tests.
 */
@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModuleHilt {

    @Singleton
    @Provides
    fun provideAuthRepository(
        @RemoteModuleHilt.RemoteDataSource api: ApiService,
        @LocalModuleHilt.LocalDataSource useDao: UserDao,
        @LocalModuleHilt.LocalDataSource messageDao: MessageDao
    ): AuthRepository {
        return AuthRepositoryImpl(api, useDao, messageDao)
    }
}