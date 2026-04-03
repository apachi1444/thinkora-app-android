package com.apachi.auraskin.feature.drawer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ZoomDrawer(
    isDrawerOpen: Boolean,
    onCloseDrawer: () -> Unit,
    drawerContent: @Composable () -> Unit,
    drawerBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    
    // Animate scale
    val scale by animateFloatAsState(
        targetValue = if (isDrawerOpen) 0.8f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "scale"
    )
    
    // Animate translation X
    // When open, move main content to the right (e.g., 280.dp or 70% of screen width)
    val translationXValue = with(density) { 280.dp.toPx() }
    val targetTranslationX = if (isDrawerOpen) translationXValue else 0f
    
    val translationX by animateFloatAsState(
        targetValue = targetTranslationX,
        animationSpec = tween(durationMillis = 300),
        label = "translationX"
    )
    
    // Animate corner radius
    val cornerRadius by animateFloatAsState(
        targetValue = if (isDrawerOpen) 32f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "cornerRadius"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(drawerBackgroundColor) // Background behind drawer
    ) {
        // Drawer Content (Behind Main Content)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawerContent()
        }

        // Main Content (Foreground)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.translationX = translationX
                    shape = RoundedCornerShape(cornerRadius.dp)
                    clip = true
                    shadowElevation = if (isDrawerOpen) 16.dp.toPx() else 0f
                }
                .background(MaterialTheme.colorScheme.background)
        ) {
            content()
            
            // Overlay to handle clicks when drawer is open
            if (isDrawerOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                             onCloseDrawer()
                        }
                )
            }
        }
    }
}
