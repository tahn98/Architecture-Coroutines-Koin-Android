package com.sg.base.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sg.data.datasource.local.AppDatabase
import com.sg.domain.util.PrefUtil
import org.koin.dsl.module

const val PREFS_FILENAME = "com.Base"

val localModule = module {
    single { Gson() }
    single { provideSharedPreference(get()) }
    single { providePreferenceHelper(get(), get(), get()) }

    single { AppDatabase.getInstance(get()) }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().messageDao() }
}

fun providePreferenceHelper(context: Context,
                            sharedPreferences: SharedPreferences, gSon: Gson) =
    PrefUtil(context, sharedPreferences, gSon)

fun provideSharedPreference(context: Context): SharedPreferences =
    context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)