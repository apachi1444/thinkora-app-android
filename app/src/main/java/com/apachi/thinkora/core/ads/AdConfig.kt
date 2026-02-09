package com.apachi.thinkora.core.ads

/**
 * Configuration for Ads.
 * Can be extended to fetch from Remote Config.
 */
object AdConfig {
    // Global switch to enable/disable ads
    var isAdEnabled: Boolean = true
    
    // Test Ad Unit IDs (Replace with Real IDs in Production)
    const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111" // Test Banner
    const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712" // Test Interstitial
}
