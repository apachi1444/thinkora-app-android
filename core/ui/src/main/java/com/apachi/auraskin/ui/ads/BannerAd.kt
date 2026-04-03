package com.apachi.auraskin.ui.ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.apachi.auraskin.data.ads.AdManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAd(
    adManager: AdManager,
    modifier: Modifier = Modifier
) {
    // Observe enablement status
    val isEnabled by adManager.isAdEnabled.collectAsState()

    if (isEnabled) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White), // Background for the ad area
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = adManager.getBannerAdUnitId()
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
    }
}
