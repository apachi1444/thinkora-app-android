package com.plcoding.widgetswithcompose.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.plcoding.widgetswithcompose.domain.model.Quote

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            // Placeholder for Bottom Navigation if needed
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Daily Streak: ${state.streak.currentStreak} 🔥")
            Spacer(modifier = Modifier.height(32.dp))

            state.dailyQuote?.let { quote ->
                QuoteCard(
                    quote = quote,
                    onToggleFavorite = { viewModel.onEvent(HomeEvent.ToggleFavorite) },
                    onMarkRead = { viewModel.onEvent(HomeEvent.MarkAsRead) }
                )
            } ?: CircularProgressIndicator()
        }
    }
}

@Composable
fun QuoteCard(
    quote: Quote,
    onToggleFavorite: () -> Unit,
    onMarkRead: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\"${quote.content}\"",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "- ${quote.author}",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (quote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (quote.isFavorite) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                }
                
                if (!quote.isRead) {
                    Button(onClick = onMarkRead) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mark Read")
                    }
                } else {
                    OutlinedButton(onClick = {}, enabled = false) {
                        Text("Read ✓")
                    }
                }
            }
        }
    }
}
