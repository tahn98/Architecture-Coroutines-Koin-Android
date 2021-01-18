package com.sg.core.di

import android.content.Context
import com.google.gson.Gson
import com.sg.core.data.local.AppDatabase
import com.sg.core.data.local.MessageDao
import com.sg.core.data.local.UserDao
import com.sg.core.util.PrefUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Module to tell Hilt how to provide instances of types that cannot be constructor-injected.
 *
 * As these types are scoped to the application lifecycle using @Singleton, they're installed
 * in Hilt's ApplicationComponent.
 */
@Module
@InstallIn(ApplicationComponent::class)
object LocalModuleHilt {

    const val PREFS_FILENAME = "com.sg.covid"

//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class LocalDataSource

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context.applicationContext)

    @Singleton
    @Provides
    fun providePreferenceHelper(
        @ApplicationContext context: Context
    ): PrefUtil {
        val gSon = Gson()
        val sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        return PrefUtil(context, sharedPreferences, gSon)
    }

    /** Local Service provider */
    @Singleton
//    @LocalDataSource
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
//    @LocalDataSource
    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao {
        return database.messageDao()
    }

}