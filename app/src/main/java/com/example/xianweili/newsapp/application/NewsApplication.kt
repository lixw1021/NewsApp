package com.example.xianweili.newsapp.application

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(this.javaClass.simpleName, "NewsApplication onCreate")
    }
}