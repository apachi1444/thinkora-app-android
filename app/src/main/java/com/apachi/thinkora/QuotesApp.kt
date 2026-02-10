package com.apachi.thinkora

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

import com.apachi.thinkora.data.ads.AdManager
import javax.inject.Inject

@HiltAndroidApp
class QuotesApp : Application() {
    @Inject
    lateinit var adManager: AdManager

    override fun onCreate() {
        super.onCreate()
        adManager.initialize(this)
    }
}
