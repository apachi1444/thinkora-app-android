package com.apachi.thinkora.ui.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat

fun ComponentActivity.setEdgeToEdgeConfig() {
    // Configure window insets
    WindowCompat.setDecorFitsSystemWindows(window, false)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Force the 3-button navigation bar to be transparent
        // See: https://developer.android.com/develop/ui/views/layout/edge-to-edge#create-transparent
        window.isNavigationBarContrastEnforced = false
    }
}
