package com.apachi.auraskin.feature.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apachi.auraskin.core.designsystem.component.AuraSkinButton
import com.apachi.auraskin.core.designsystem.component.AuraSkinTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommitmentScreen(state: OnboardingState, viewModel: OnboardingViewModel) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showTimePicker by remember { mutableStateOf(false) }
    var reminderHour by remember { mutableStateOf(20) } 
    var reminderMinute by remember { mutableStateOf(0) }

    Column {
        Text(
            text = "Are you ready to commit?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Consistency is the single most important factor in skincare. Let us gently remind you.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(48.dp))
        
        AuraSkinTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(OnboardingEvent.EnterName(it)) },
            label = { Text("What should we call you?") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Daily Reminder", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                }
                
                if (notificationsEnabled) {
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { showTimePicker = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Time", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "%02d:%02d".format(reminderHour, reminderMinute),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        AuraSkinButton(
            onClick = { 
                viewModel.onEvent(OnboardingEvent.SubmitWithNotifications(notificationsEnabled, reminderHour, reminderMinute)) 
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.name.isNotBlank()
        ) {
            Text("Complete Journey Setup", fontWeight = FontWeight.Bold)
        }

        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(initialHour = reminderHour, initialMinute = reminderMinute, is24Hour = false)
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        reminderHour = timePickerState.hour
                        reminderMinute = timePickerState.minute
                        showTimePicker = false
                    }) { Text("Confirm") }
                },
                text = { TimePicker(state = timePickerState) }
            )
        }
    }
}
