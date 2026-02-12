package com.apachi.thinkora.feature.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

import androidx.compose.ui.res.stringResource
import com.apachi.thinkora.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var habitToDelete by remember { mutableStateOf<com.apachi.thinkora.domain.model.Habit?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(HabitsEvent.ShowAddDialog) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.habits_add_habit)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.habits_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
               items(state.habits, key = { it.id }) { habit ->
                   SwipeToDeleteContainer(
                       onDelete = {
                           habitToDelete = habit
                           showDeleteConfirmation = true
                       }
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

        if (state.isAddDialogVisible) {
            AddHabitDialog(
                onDismiss = { viewModel.onEvent(HabitsEvent.HideAddDialog) },
                onConfirm = { name, streak -> 
                    viewModel.onEvent(HabitsEvent.AddHabit(name, streak))
                    viewModel.onEvent(HabitsEvent.HideAddDialog)
                }
            )
        }

        if (showDeleteConfirmation && habitToDelete != null) {
            val confirmedHabitName = habitToDelete!!.name
            val deletedConfirmMsg = stringResource(R.string.habits_deleted_confirm, confirmedHabitName)
            val undoLabel = stringResource(R.string.habits_undo)

            DeleteConfirmationDialog(
                habitName = confirmedHabitName,
                onConfirm = {
                    val deletedHabit = habitToDelete!!
                    viewModel.onEvent(HabitsEvent.DeleteHabit(deletedHabit.id))
                    showDeleteConfirmation = false
                    habitToDelete = null
                    
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = deletedConfirmMsg,
                            actionLabel = undoLabel,
                            duration = SnackbarDuration.Long
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(HabitsEvent.AddHabit(deletedHabit.name, deletedHabit.streak))
                        }
                    }
                },
                onDismiss = {
                    showDeleteConfirmation = false
                    habitToDelete = null
                }
            )
        }

        if (state.isWidgetTutorialVisible) {
            WidgetDiscoveryDialog(
                onDismiss = { viewModel.onEvent(HabitsEvent.HideWidgetTutorial) }
            )
        }
    }
}

@Composable
fun WidgetDiscoveryDialog(
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = stringResource(R.string.habits_widget_tutorial_title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(stringResource(R.string.habits_widget_tutorial_desc))
        },
        confirmButton = {
            Button(
                onClick = {
                    val appWidgetManager = android.appwidget.AppWidgetManager.getInstance(context)
                    val myProvider = android.content.ComponentName(context, "com.apachi.thinkora.feature.habits.widget.HabitsWidgetReceiver")
                    
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && appWidgetManager.isRequestPinAppWidgetSupported) {
                        appWidgetManager.requestPinAppWidget(myProvider, null, null)
                    } else {
                        android.widget.Toast.makeText(context, context.getString(R.string.habits_widget_unsupported), android.widget.Toast.LENGTH_SHORT).show()
                    }
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.habits_widget_add))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.habits_widget_dismiss))
            }
        }
    )
}

@Composable
fun HabitItem(
    habit: com.apachi.thinkora.domain.model.Habit,
    onIncrementClick: ((String) -> Unit)? = null
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
           Column(modifier = Modifier.weight(1f)) {
               Text(
                   text = habit.name,
                   style = MaterialTheme.typography.titleMedium,
                   fontWeight = FontWeight.SemiBold
               )
           }
           
           Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
               Icon(
                   imageVector = Icons.Default.Build,
                   contentDescription = null, 
                   tint = Color(0xFFF97316) // Keep streak orange as it's branding/status
               )
               Text(
                   text = "${habit.streak}",
                   style = MaterialTheme.typography.titleMedium,
                   fontWeight = FontWeight.Bold,
                   color = Color(0xFFF97316)
               )
               
               if (onIncrementClick != null) {
                   IconButton(
                       onClick = { onIncrementClick(habit.id) },
                       modifier = Modifier.size(32.dp)
                   ) {
                       Icon(
                           imageVector = Icons.Default.Add,
                           contentDescription = stringResource(R.string.habits_increment),
                           tint = MaterialTheme.colorScheme.primary
                       )
                   }
               }
           }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart || dismissValue == DismissValue.DismissedToEnd) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            kotlinx.coroutines.delay(300)
            onDelete()
        }
    }

    SwipeToDismiss(
        state = dismissState,
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd, DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                null -> Color.Transparent
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (dismissState.dismissDirection != null) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.common_delete),
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        dismissContent = { content() },
        directions = setOf(DismissDirection.EndToStart)
    )
}

@Composable
fun DeleteConfirmationDialog(
    habitName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = { 
            Text(
                text = stringResource(R.string.habits_delete_title),
                fontWeight = FontWeight.Bold
            ) 
        },
        text = { 
            Text(stringResource(R.string.habits_delete_message, habitName)) 
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(stringResource(R.string.common_delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    )
}

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var streak by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.habits_new_habit)) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.habits_name_label)) },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = streak,
                    onValueChange = { if (it.all { char -> char.isDigit() }) streak = it },
                    label = { Text(stringResource(R.string.habits_streak_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (name.isNotBlank()) {
                        onConfirm(name, streak.toIntOrNull() ?: 0)
                    }
                }
            ) {
                Text(stringResource(R.string.common_add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    )
}
