package com.sg.base

import androidx.lifecycle.LifecycleObserver
import com.sg.core.CoreApplication
//import com.sg.base.di.appModules
import dagger.hilt.android.HiltAndroidApp
//import org.koin.core.module.Module

@HiltAndroidApp
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

//    override fun addModules(): List<Module> {
//        return listOf()
//    }
}