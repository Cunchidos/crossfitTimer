package com.crossfit.timer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CrossFitTimerApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
