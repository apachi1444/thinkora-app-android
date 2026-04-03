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
            text = "What's your primary skin focus?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Let's tailor your journey to what matters most to you right now.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        AuraSkinSelectionCard(
            title = "Clear Breakouts",
            subtitle = "ACTIVE TREATMENT",
            isSelected = state.skinFocus == "Clear Breakouts",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectSkinFocus("Clear Breakouts")) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuraSkinSelectionCard(
            title = "Reduce Redness",
            subtitle = "CALM & SOOTHE",
            isSelected = state.skinFocus == "Reduce Redness",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectSkinFocus("Reduce Redness")) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuraSkinSelectionCard(
            title = "Build a Routine",
            subtitle = "CONSISTENCY FOCUS",
            isSelected = state.skinFocus == "Build a Routine",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectSkinFocus("Build a Routine")) }
        )
    }
}
