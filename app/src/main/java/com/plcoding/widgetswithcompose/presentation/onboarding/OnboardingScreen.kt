package com.plcoding.widgetswithcompose.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.widgetswithcompose.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Observe UI Events (Navigation)
    // Note: In real app, use LaunchedEffect with flow collection.
    // Ideally pass a callback 'onNavigate' to the Screen.
    // For simplicity, I'll rely on state or passing event up.
    // But ViewModel emits event.
    
    androidx.compose.runtime.LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is OnboardingUiEvent.OnboardingCompleted -> {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.OnboardingScreen.route) { inclusive = true }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to Quotes", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(OnboardingEvent.EnterName(it)) },
            label = { Text("Your Name") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Select Interests:")
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.availableInterests) { interest ->
                FilterChip(
                    selected = state.selectedInterests.contains(interest),
                    onClick = { viewModel.onEvent(OnboardingEvent.ToggleInterest(interest)) },
                    label = { Text(interest) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.onEvent(OnboardingEvent.Submit) },
            enabled = state.name.isNotBlank() && state.selectedInterests.isNotEmpty()
        ) {
            Text("Get Started")
        }
    }
}
