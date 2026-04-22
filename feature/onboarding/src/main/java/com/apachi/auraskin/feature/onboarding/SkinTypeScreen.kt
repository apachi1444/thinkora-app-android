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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun SkinTypeScreen(state: OnboardingState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "Your Skin Identity",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Understanding your surface helps us refine your habit recommendations.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(40.dp))
        
        val types = listOf("Oily", "Dry", "Combination", "Normal")
        
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            types.chunked(2).forEach { rowTypes ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowTypes.forEach { type ->
                        AuraSkinSelectionCard(
                            title = type,
                            subtitle = null,
                            isSelected = state.skinType == type,
                            onClick = { viewModel.onEvent(OnboardingEvent.SelectSkinType(type)) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
