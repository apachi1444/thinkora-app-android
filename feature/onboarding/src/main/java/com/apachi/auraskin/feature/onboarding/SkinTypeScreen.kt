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
fun SkinTypeScreen(state: OnboardingState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "What is your skin type?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "This helps us recommend the right tracking metrics.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(48.dp))
        
        val types = listOf("Oily", "Dry", "Combination", "Normal")
        types.forEach { type ->
            AuraSkinSelectionCard(
                title = type,
                subtitle = null,
                isSelected = state.skinType == type,
                onClick = { viewModel.onEvent(OnboardingEvent.SelectSkinType(type)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
