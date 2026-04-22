package com.apachi.auraskin.feature.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apachi.auraskin.core.designsystem.component.AuraSkinButton
import com.apachi.auraskin.core.designsystem.component.AuraSkinTextField
import com.apachi.auraskin.domain.navigation.Screen
import com.apachi.auraskin.designsystem.R as DesignR
import androidx.compose.animation.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is OnboardingUiEvent.OnboardingCompleted) {
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.OnboardingScreen.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Subtle Luminous Aura Background Effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.4f)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(0f, 0f)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .systemBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Premium Progress Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) { index ->
                    val isPast = state.currentPage > index
                    val isCurrent = state.currentPage == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isPast -> MaterialTheme.colorScheme.primary
                                    isCurrent -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "STEP ${state.currentPage + 1} OF 4",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Personalization",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(modifier = Modifier.weight(1f)) {
                AnimatedContent(
                    targetState = state.currentPage,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                        } else {
                            (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                        }.using(SizeTransform(clip = false))
                    },
                    label = "OnboardingNav"
                ) { page ->
                    when (page) {
                        0 -> FocusScreen(state, viewModel)
                        1 -> SkinTypeScreen(state, viewModel)
                        2 -> RoutineScreen(state, viewModel)
                        3 -> CommitmentScreen(state, viewModel)
                    }
                }
            }

        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.currentPage > 0) {
                TextButton(onClick = { viewModel.onEvent(OnboardingEvent.PreviousPage) }) {
                    Text("Back", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Spacer(modifier = Modifier.width(64.dp))
            }

            val isNextEnabled = when (state.currentPage) {
                0 -> state.skinFocus.isNotEmpty()
                1 -> state.skinType.isNotEmpty()
                2 -> state.routineType.isNotEmpty()
                else -> true // Commitment handles its own submit
            }

            AuraSkinButton(
                onClick = { 
                    if (state.currentPage < 3) viewModel.onEvent(OnboardingEvent.NextPage) 
                },
                modifier = Modifier.weight(1f).padding(start = if (state.currentPage > 0) 16.dp else 0.dp),
                enabled = isNextEnabled && state.currentPage < 3
            ) {
                Text("Continue", fontWeight = FontWeight.Bold)
            }
            
            // Note: Page 4 (CommitmentScreen) hides this default Next button via layout or handles its own submit
        }
    }
}


