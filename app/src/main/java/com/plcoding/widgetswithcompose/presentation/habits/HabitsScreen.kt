package com.plcoding.widgetswithcompose.presentation.habits

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(HabitsEvent.ShowAddDialog) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FC))
                .padding(16.dp)
        ) {
            Text(
                text = "Your Habits",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
               items(state.habits, key = { it.id }) { habit ->
                   SwipeToDeleteContainer(
                       onDelete = { viewModel.onEvent(HabitsEvent.DeleteHabit(habit.id)) }
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
    }
}

@Composable
fun HabitItem(
    habit: com.plcoding.widgetswithcompose.domain.model.Habit,
    onIncrementClick: ((String) -> Unit)? = null
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                   Icons.Default.Build,
                   contentDescription = null, 
                   tint = Color(0xFFF97316)
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
                           Icons.Default.Add,
                           contentDescription = "Increment",
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
                DismissDirection.StartToEnd, DismissDirection.EndToStart -> Color(0xFFEF4444)
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
                        contentDescription = "Delete",
                        tint = Color.White,
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
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var streak by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Habit") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Habit Name") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = streak,
                    onValueChange = { if (it.all { char -> char.isDigit() }) streak = it },
                    label = { Text("Current Streak") },
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
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
