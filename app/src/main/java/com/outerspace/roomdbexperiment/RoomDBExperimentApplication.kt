package com.outerspace.roomdbexperiment

import android.app.Application
import android.content.Context

class RoomDBExperimentApplication: Application() {
    companion object {
        lateinit var appC: Context
        fun appContext() = appC
    }

    override fun onCreate() {
        super.onCreate()
        appC = applicationContext
    }
}