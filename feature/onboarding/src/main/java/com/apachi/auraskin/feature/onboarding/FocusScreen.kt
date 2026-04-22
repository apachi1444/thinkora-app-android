package com.apachi.auraskin.feature.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apachi.auraskin.core.designsystem.component.AuraSkinSelectionCard

@Composable
fun FocusScreen(state: OnboardingState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "Your Skin's Primary Focus",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "We will tailor your journey to what matters most to your complexion right now.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        AuraSkinSelectionCard(
            title = "Clear Breakouts",
            subtitle = "Soothe active inflammation",
            isSelected = state.skinFocus == "Clear Breakouts",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectSkinFocus("Clear Breakouts")) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        AuraSkinSelectionCard(
            title = "Reduce Redness",
            subtitle = "Calm reactive conditions",
            isSelected = state.skinFocus == "Reduce Redness",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectSkinFocus("Reduce Redness")) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        AuraSkinSelectionCard(
            title = "Consistency First",
            subtitle = "Build a sustainable routine",
            isSelected = state.skinFocus == "Build a Routine",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectSkinFocus("Build a Routine")) }
        )
    }
}
