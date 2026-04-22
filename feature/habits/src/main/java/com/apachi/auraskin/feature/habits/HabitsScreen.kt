package com.apachi.auraskin.feature.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.FaceRetouchingNatural
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Loop
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apachi.auraskin.designsystem.R
import com.apachi.auraskin.domain.model.Habit
import com.apachi.auraskin.domain.model.HabitCategory
import com.apachi.auraskin.feature.habits.widget.HabitsWidgetReceiver
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var cancelledHabitId by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf(HabitCategory.SKINCARE_MORNING) }

    val filteredHabits = state.habits.filter { it.category == selectedCategory }

    LaunchedEffect(cancelledHabitId) {
        cancelledHabitId?.let {
            kotlinx.coroutines.delay(100)
            cancelledHabitId = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.large) // 24dp for generic cards/buttons
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        )
                    )
                    .clickable { viewModel.onEvent(HabitsEvent.ShowAddDialog) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.habits_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Track your daily rituals and skincare flow.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Category Filter Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val categories = listOf(
                    HabitCategory.SKINCARE_MORNING to R.string.habits_morning,
                    HabitCategory.SKINCARE_NIGHT to R.string.habits_night,
                    HabitCategory.LIFESTYLE to R.string.habits_lifestyle
                )
                
                items(categories) { (category, stringRes) ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .background(
                                if (isSelected) 
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                        )
                                    )
                                else 
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = stringResource(stringRes),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Habits List
            if (filteredHabits.isEmpty()) {
                Text(
                    text = "No rituals planned for this phase yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(filteredHabits, key = { it.id }) { habit ->
                        SwipeToDeleteContainer(
                            onDelete = {
                                habitToDelete = habit
                                showDeleteConfirmation = true
                            },
                            resetTrigger = cancelledHabitId == habit.id
                        ) {
                            HabitItem(
                                habit = habit,
                                onIncrementClick = { habitId ->
                                    viewModel.onEvent(HabitsEvent.IncrementStreak(habitId))
                                }
                            )
                        }
                    }
                }
            }
        }

        // Dialogs
        if (state.isAddDialogVisible) {
            AddHabitDialog(
                onDismiss = { viewModel.onEvent(HabitsEvent.HideAddDialog) },
                onConfirm = { name, streak -> 
                    viewModel.onEvent(HabitsEvent.AddHabit(name, streak)) // Implicitly defaults to LIFESTYLE in ViewModel if unchanged, but UI adds.
                    viewModel.onEvent(HabitsEvent.HideAddDialog)
                }
            )
        }

        if (showDeleteConfirmation && habitToDelete != null) {
            DeleteConfirmationDialog(
                habitName = habitToDelete!!.name,
                onConfirm = {
                    val deletedHabit = habitToDelete!!
                    viewModel.onEvent(HabitsEvent.DeleteHabit(deletedHabit.id))
                    showDeleteConfirmation = false
                    habitToDelete = null
                    
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar("${deletedHabit.name} deleted", "Undo", duration = SnackbarDuration.Long)
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(HabitsEvent.AddHabit(deletedHabit.name, deletedHabit.streak))
                        }
                    }
                },
                onDismiss = {
                    val id = habitToDelete?.id
                    showDeleteConfirmation = false
                    habitToDelete = null
                    id?.let { cancelledHabitId = it }
                }
            )
        }

        if (state.isWidgetTutorialVisible) {
            WidgetDiscoveryDialog { viewModel.onEvent(HabitsEvent.HideWidgetTutorial) }
        }
    }
}

@Composable
private fun HabitItem(
    habit: Habit,
    onIncrementClick: ((String) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large) // 24dp
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.FaceRetouchingNatural, // Default placeholder
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocalFireDepartment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${habit.streak} day streak",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // CheckButton
        if (onIncrementClick != null) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .clickable { onIncrementClick(habit.id) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Complete",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteContainer(
    onDelete: () -> Unit,
    resetTrigger: Boolean = false,
    content: @Composable () -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart || dismissValue == DismissValue.DismissedToEnd) {
                isRemoved = true
                true
            } else false
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            kotlinx.coroutines.delay(300)
            onDelete()
        }
    }
    LaunchedEffect(resetTrigger) { if (resetTrigger) dismissState.reset() }

    SwipeToDismiss(
        state = dismissState,
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd, DismissDirection.EndToStart -> MaterialTheme.colorScheme.errorContainer
                null -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(color)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (dismissState.dismissDirection != null) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        },
        dismissContent = { content() },
        directions = setOf(DismissDirection.EndToStart)
    )
}

// Dialogs remain largely functional but apply standard theming
@Composable
fun DeleteConfirmationDialog(habitName: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
        title = { Text("Delete Habit?", fontWeight = FontWeight.Bold) },
        text = { Text("Are you sure you want to delete \"$habitName\"?") },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Delete")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AddHabitDialog(onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var streak by remember { mutableStateOf("0") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Habit") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Habit Name") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = streak, onValueChange = { if (it.all { c -> c.isDigit() }) streak = it }, label = { Text("Streak") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            }
        },
        confirmButton = { Button(onClick = { if (name.isNotBlank()) onConfirm(name, streak.toIntOrNull() ?: 0) }) { Text("Add") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun WidgetDiscoveryDialog(onDismiss: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Build, null) },
        title = { Text("Add a Widget!") },
        text = { Text("Track habits easily without opening the app!") },
        confirmButton = {
            Button(onClick = {
                val appWidgetManager = android.appwidget.AppWidgetManager.getInstance(context)
                val myProvider = android.content.ComponentName(context, HabitsWidgetReceiver::class.java)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && appWidgetManager.isRequestPinAppWidgetSupported) {
                    appWidgetManager.requestPinAppWidget(myProvider, null, null)
                }
                onDismiss()
            }) { Text("Add Widget") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Got it") } }
    )
}
