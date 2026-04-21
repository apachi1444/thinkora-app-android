package com.apachi.auraskin.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat

// ============================================================
// AuraSkin Luminous Calm — Theme Configuration
// Derived from Stitch design tokens
// ============================================================

private val AuraSkinDarkColorScheme = darkColorScheme(
    primary = AuraSkinDarkPrimary,
    onPrimary = AuraSkinDarkOnPrimary,
    secondary = AuraSkinDarkPrimary,
    tertiary = AuraSkinDarkStreakContainer,
    tertiaryContainer = AuraSkinDarkStreakContainer,
    background = AuraSkinDarkBackground,
    surface = AuraSkinDarkSurface,
    surfaceVariant = AuraSkinDarkSurfaceVariant,
    onBackground = AuraSkinDarkOnSurface,
    onSurface = AuraSkinDarkOnSurface,
    onSurfaceVariant = AuraSkinDarkOnSurfaceVariant,
    outline = AuraSkinDarkOutline,
    outlineVariant = AuraSkinDarkOutline,
    error = AuraSkinDarkError,
    onError = AuraSkinDarkOnPrimary
)

private val AuraSkinLightColorScheme = lightColorScheme(
    primary = AuraSkinLightPrimary,
    onPrimary = AuraSkinLightOnPrimary,
    secondary = AuraSkinLightPrimary,
    tertiary = AuraSkinLightStreakContainer,
    tertiaryContainer = AuraSkinLightStreakContainer,
    background = AuraSkinLightBackground,
    surface = AuraSkinLightSurface,
    surfaceVariant = AuraSkinLightSurfaceVariant,
    onBackground = AuraSkinLightOnSurface,
    onSurface = AuraSkinLightOnSurface,
    onSurfaceVariant = AuraSkinLightOnSurfaceVariant,
    outline = AuraSkinLightOutline,
    outlineVariant = AuraSkinLightOutline,
    error = AuraSkinLightError,
    onError = AuraSkinLightOnPrimary
)

// Stitch shape tokens: Cards 24px, Buttons 16px
private val AuraSkinShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),      // Buttons, chips
    large = RoundedCornerShape(24.dp),       // Cards, containers
    extraLarge = RoundedCornerShape(32.dp)   // Full dialogs, drawer corners
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
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AuraSkinShapes,
        content = content
    )
}