package com.apachi.auraskin.feature.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.apachi.auraskin.designsystem.R as DesignR
import com.apachi.auraskin.domain.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingIntroScreen(
    navController: NavController,
) {
    val pages = listOf(
        OnboardingPage(
            title = stringResource(DesignR.string.onboarding_welcome_title),
            description = stringResource(DesignR.string.onboarding_welcome_desc),
            imageVector = Icons.Default.ArrowForward
        ),
        OnboardingPage(
            title = stringResource(DesignR.string.onboarding_motivation_title),
            description = stringResource(DesignR.string.onboarding_motivation_desc),
            imageVector = Icons.Default.Star
        ),
        OnboardingPage(
            title = stringResource(DesignR.string.onboarding_widget_title),
            description = stringResource(DesignR.string.onboarding_widget_desc),
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
            Text(if (pagerState.currentPage < pages.size - 1) stringResource(DesignR.string.onboarding_next) else stringResource(DesignR.string.onboarding_get_started))
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
        
        Text(text = stringResource(DesignR.string.onboarding_name_question), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(OnboardingEvent.EnterName(it)) },
            label = { Text(stringResource(DesignR.string.onboarding_name_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(Screen.OnboardingInterests.route) },
            enabled = state.name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(DesignR.string.onboarding_next))
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
            text = stringResource(DesignR.string.onboarding_interests_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(DesignR.string.onboarding_interests_desc),
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
                    label = { 
                        val labelRes = when(interest) {
                            "Business" -> DesignR.string.category_business
                            "Life" -> DesignR.string.category_life
                            "Sports" -> DesignR.string.category_sports
                            "Tech" -> DesignR.string.category_tech
                            else -> null
                        }
                        Text(labelRes?.let { stringResource(it) } ?: interest)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(Screen.OnboardingNotifications.route) },
            enabled = state.selectedInterests.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(DesignR.string.onboarding_next))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingNotificationsScreen(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    val state by viewModel.state.collectAsState()
    var notificationsEnabled by remember { mutableStateOf(true) }
    var reminderHour by remember {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.HOUR_OF_DAY, 1)
        mutableStateOf(cal.get(java.util.Calendar.HOUR_OF_DAY))
    }
    var reminderMinute by remember {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.HOUR_OF_DAY, 1)
        mutableStateOf(cal.get(java.util.Calendar.MINUTE))
    }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is OnboardingUiEvent.OnboardingCompleted -> {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.OnboardingIntro.route) { inclusive = true }
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
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(DesignR.string.onboarding_notifications_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(DesignR.string.onboarding_notifications_desc),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(DesignR.string.onboarding_notifications_enable))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (notificationsEnabled) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker = true }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(DesignR.string.onboarding_reminder_time))
                Text(
                    text = "%02d:%02d".format(reminderHour, reminderMinute),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            if (showTimePicker) {
                val timePickerState = rememberTimePickerState(
                    initialHour = reminderHour,
                    initialMinute = reminderMinute,
                    is24Hour = true
                )
                AlertDialog(
                    onDismissRequest = { showTimePicker = false },
                    confirmButton = {
                        Button(onClick = {
                            reminderHour = timePickerState.hour
                            reminderMinute = timePickerState.minute
                            showTimePicker = false
                        }) {
                            Text(stringResource(DesignR.string.common_done))
                        }
                    },
                    title = { Text(stringResource(DesignR.string.onboarding_reminder_time)) },
                    text = {
                        TimePicker(state = timePickerState)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                viewModel.onEvent(
                    OnboardingEvent.SubmitWithNotifications(
                        enabled = notificationsEnabled,
                        hour = reminderHour,
                        minute = reminderMinute
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(DesignR.string.onboarding_continue))
        }
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageVector: ImageVector
)

