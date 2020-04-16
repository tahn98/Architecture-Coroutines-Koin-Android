package com.sg.base

import androidx.lifecycle.LifecycleObserver
import com.sg.core.CoreApplication
import com.sg.base.di.appModules
import org.koin.core.module.Module

class MainApplication : CoreApplication(), LifecycleObserver {

    companion object {
        lateinit var instance: MainApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getInstance() = instance

    override fun addModules(): List<Module> {
        return listOf(appModules)
    }
}