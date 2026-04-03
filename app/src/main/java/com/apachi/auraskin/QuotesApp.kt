package com.apachi.auraskin

import android.app.Application
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import com.apachi.auraskin.data.ads.AdManager
import javax.inject.Inject

@HiltAndroidApp
class QuotesApp : Application(), Configuration.Provider {
    @Inject
    lateinit var adManager: AdManager

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        adManager.initialize(this)
    }
}
