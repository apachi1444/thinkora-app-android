package com.apachi.thinkora.core.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AdManager {

    private var interstitialAd: InterstitialAd? = null
    private val _isAdEnabled = MutableStateFlow(AdConfig.isAdEnabled)
    override val isAdEnabled: StateFlow<Boolean> = _isAdEnabled.asStateFlow()

    override fun initialize(context: Context) {
        if (!AdConfig.isAdEnabled) return
        
        MobileAds.initialize(context) { }
        loadInterstitial(context)
    }

    override fun loadInterstitial(context: Context) {
        if (!AdConfig.isAdEnabled) return

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            AdConfig.INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@AdMobManager.interstitialAd = interstitialAd
                }
            }
        )
    }

    override fun showInterstitial(activity: Activity, onAdDismissed: () -> Unit) {
        if (!AdConfig.isAdEnabled || interstitialAd == null) {
            onAdDismissed()
            return
        }

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitial(activity) // Reload for next time
                onAdDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                interstitialAd = null
                onAdDismissed()
            }
        }

        interstitialAd?.show(activity)
    }

    override fun getBannerAdUnitId(): String {
        return AdConfig.BANNER_AD_UNIT_ID
    }
}
