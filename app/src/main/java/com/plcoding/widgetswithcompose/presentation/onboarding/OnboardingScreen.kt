package com.plcoding.widgetswithcompose.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.plcoding.widgetswithcompose.presentation.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingIntroScreen(
    navController: NavController,
) {
    val pages = listOf(
        OnboardingPage(
            title = "Welcome to Quotes",
            description = "Discover daily inspiration and wisdom from the greatest minds.",
            imageVector = Icons.Default.ArrowForward
        ),
        OnboardingPage(
            title = "Daily Motivation",
            description = "Get a new quote every day to start your morning with positivity.",
            imageVector = Icons.Default.Star
        ),
        OnboardingPage(
            title = "Widget Integration",
            description = "Add widgets to your home screen for instant access to your favorite quotes.",
            imageVector = Icons.Default.Check
        )
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { position ->
            OnboardingPageContent(page = pages[position])
        }

        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else {
                    navController.navigate(Screen.OnboardingName.route)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (pagerState.currentPage < pages.size - 1) "Next" else "Get Started")
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = page.imageVector,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingNameScreen(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(text = "What's your name?", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(OnboardingEvent.EnterName(it)) },
            label = { Text("Your Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(Screen.OnboardingInterests.route) },
            enabled = state.name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingInterestsScreen(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is OnboardingUiEvent.OnboardingCompleted -> {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.OnboardingIntro.route) { inclusive = true } // Pop everything from intro
                        // Also pop the graph start if structured that way
                    }
                }
                else -> Unit
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
        Text(
            text = "Select your interests",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tailor your daily quotes.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        // Using FlowRow if available or multiple rows. Since build.gradle didn't show FlowRow availability (depends on compose version),
        // I'll stick to simple Column with Rows or LazyRow. The original code used LazyRow.
        // Let's improve it to be a bit more grid-like or wrap-like if possible, but safe is LazyRow or Column of Rows.
        // Assuming small number of interests, a LazyRow is fine.
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
            enabled = state.selectedInterests.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete Setup")
        }
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageVector: ImageVector
)

