package com.sg.base

import com.sg.core.CoreApplication
import dagger.hilt.android.HiltAndroidApp

class MainApplication : CoreApplication() {

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