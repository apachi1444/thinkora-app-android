package com.apachi.thinkora.feature.quotes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuoteScreen(
    navController: NavController,
    viewModel: CustomQuotesViewModel
) {
    var content by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Personal") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Personal Quote") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Quote Content") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Category Selection (Simple dropdown or chips)
            Text("Category", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Personal", "Motivation", "Work", "Life").forEach { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (content.isNotBlank()) {
                        viewModel.addQuote(content, author.ifBlank { "Me" }, category)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enable = content.isNotBlank()
            ) {
                Text("Save Quote")
            }
        }
    }
}
