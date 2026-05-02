package com.apachi.auraskin.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp), // For Buttons
    large = RoundedCornerShape(24.dp),  // For Cards
    extraLarge = RoundedCornerShape(50) // For Avatars (50%)
)
