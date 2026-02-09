package com.apachi.thinkora.core.ads

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for Ad Management to allow easy switching or disabling of Ad SDKs.
 */
interface AdManager {
    fun initialize(context: Context)
    fun loadInterstitial(context: Context)
    fun showInterstitial(activity: Activity, onAdDismissed: () -> Unit)
    fun getBannerAdUnitId(): String
    val isAdEnabled: StateFlow<Boolean> 
}
