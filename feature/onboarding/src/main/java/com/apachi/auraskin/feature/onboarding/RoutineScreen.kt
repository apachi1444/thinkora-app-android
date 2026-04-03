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
fun RoutineScreen(state: OnboardingState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "How extensive is your current routine?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "We'll suggest habits to match your commitment level.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(48.dp))
        
        AuraSkinSelectionCard(
            title = "Minimal to None",
            subtitle = "0-1 steps",
            isSelected = state.routineType == "None",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectRoutineType("None")) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuraSkinSelectionCard(
            title = "Basic",
            subtitle = "Cleanser, Moisturizer, SPF",
            isSelected = state.routineType == "Basic",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectRoutineType("Basic")) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuraSkinSelectionCard(
            title = "Advanced",
            subtitle = "Multi-step complex routines",
            isSelected = state.routineType == "Advanced",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectRoutineType("Advanced")) }
        )
    }
}
