package com.sg.base

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.sg.base.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class MainApplication : Application(), LifecycleObserver {


    companion object {
        lateinit var instance: MainApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(getModule())
        }

        instance = this
    }

    private fun getModule(): List<Module> {
        val moduleList = arrayListOf<Module>()
        moduleList.addAll(
            listOf(
                remoteModule,
                repositoryModule,
                localModule,
                roomModule,
                viewModelModules
            )
        )
        return moduleList
    }

}