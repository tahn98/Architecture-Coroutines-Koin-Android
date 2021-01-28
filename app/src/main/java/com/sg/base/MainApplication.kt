package com.sg.base

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.sg.base.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class MainApplication : Application() {

    companion object {
        lateinit var instance: MainApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(getModule())
        }
    }

    private fun getModule(): List<Module> {
        val moduleList = arrayListOf<Module>()
        moduleList.addAll(
            listOf(
                localModule,
                remoteModule,
                repositoryModule,
                viewModelModule
            )
        )
        return moduleList
    }

}