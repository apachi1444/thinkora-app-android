package com.apachi.auraskin.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val AuraSkinDarkColorScheme = darkColorScheme(
    primary = AuraSkinDarkPrimary,
    onPrimary = AuraSkinDarkOnPrimary,
    secondary = AuraSkinDarkPrimary, // Re-using primary for secondary fallback
    tertiary = AuraSkinDarkStreakContainer,
    tertiaryContainer = AuraSkinDarkStreakContainer,
    background = AuraSkinDarkBackground,
    surface = AuraSkinDarkSurface,
    onBackground = AuraSkinDarkOnSurface,
    onSurface = AuraSkinDarkOnSurface,
    onSurfaceVariant = AuraSkinDarkOnSurfaceVariant,
    outline = AuraSkinDarkOutline
)

private val AuraSkinLightColorScheme = lightColorScheme(
    primary = AuraSkinLightPrimary,
    onPrimary = AuraSkinLightOnPrimary,
    secondary = AuraSkinLightPrimary,
    tertiary = AuraSkinLightStreakContainer,
    tertiaryContainer = AuraSkinLightStreakContainer,
    background = AuraSkinLightBackground,
    surface = AuraSkinLightSurface,
    onBackground = AuraSkinLightOnSurface,
    onSurface = AuraSkinLightOnSurface,
    onSurfaceVariant = AuraSkinLightOnSurfaceVariant,
    outline = AuraSkinLightOutline
)

@Composable
fun AuraSkinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AuraSkinDarkColorScheme else AuraSkinLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb() // typically better matching background than primary
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}