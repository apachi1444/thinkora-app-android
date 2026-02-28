package com.apachi.thinkora.core.designsystem.theme

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

private val ThinkoraDarkColorScheme = darkColorScheme(
    primary = ThinkoraDarkPrimary,
    onPrimary = ThinkoraDarkOnPrimary,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    tertiaryContainer = ThinkoraDarkStreakContainer,
    background = ThinkoraDarkBackground,
    surface = ThinkoraDarkSurface,
    onBackground = ThinkoraDarkOnSurface,
    onSurface = ThinkoraDarkOnSurface,
    onSurfaceVariant = ThinkoraDarkOnSurfaceVariant,
    outline = ThinkoraDarkOutline
)

private val ThinkoraLightColorScheme = lightColorScheme(
    primary = ThinkoraLightPrimary,
    onPrimary = ThinkoraLightOnPrimary,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    tertiaryContainer = ThinkoraLightStreakContainer,
    background = ThinkoraLightBackground,
    surface = ThinkoraLightSurface,
    onBackground = ThinkoraLightOnSurface,
    onSurface = ThinkoraLightOnSurface,
    onSurfaceVariant = ThinkoraLightOnSurfaceVariant,
    outline = ThinkoraLightOutline
)

@Composable
fun ThinkoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ThinkoraDarkColorScheme else ThinkoraLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}