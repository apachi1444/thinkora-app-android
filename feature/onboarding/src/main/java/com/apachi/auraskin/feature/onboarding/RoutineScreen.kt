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
            text = "Routine Maturity",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Your current level of ritual helps us suggest habits that feel natural, not overwhelming.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(40.dp))
        
        AuraSkinSelectionCard(
            title = "Essential",
            subtitle = "Cleanser or nothing yet",
            isSelected = state.routineType == "None",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectRoutineType("None")) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        AuraSkinSelectionCard(
            title = "Balanced",
            subtitle = "Cleanser, Moisturize, SPF",
            isSelected = state.routineType == "Basic",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectRoutineType("Basic")) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        AuraSkinSelectionCard(
            title = "Advanced",
            subtitle = "Serums, Actives, multi-step",
            isSelected = state.routineType == "Advanced",
            onClick = { viewModel.onEvent(OnboardingEvent.SelectRoutineType("Advanced")) }
        )
    }
}
