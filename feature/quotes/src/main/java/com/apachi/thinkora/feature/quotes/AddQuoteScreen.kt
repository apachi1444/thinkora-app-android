package com.apachi.thinkora.feature.quotes


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.app.Activity
import androidx.compose.material.icons.filled.ArrowBack
import com.apachi.thinkora.core.designsystem.component.ThinkoraTextField
import com.apachi.thinkora.core.designsystem.component.ThinkoraButton
import com.apachi.thinkora.core.designsystem.component.ThinkoraTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuoteScreen(
    navController: NavController,
    viewModel: CustomQuotesViewModel
) {
    var content by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Personal") }

    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            ThinkoraTopAppBar(
                title = { Text("Add Quote") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
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
            ThinkoraTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Quote Content") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            ThinkoraTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                modifier = Modifier.fillMaxWidth()
            )

            // Category Selection (Simple dropdown or chips)
            Text(
                "Category",
                style = MaterialTheme.typography.labelLarge
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val categories = listOf(
                    "Personal",
                    "Motivation",
                    "Work",
                    "Life"
                )
                categories.forEach { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ThinkoraButton(
                onClick = {
                    if (content.isNotBlank()) {
                        viewModel.addQuote(content, author.ifBlank { "Me" }, category, activity) {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = content.isNotBlank()
            ) {
                Text("Save Quote")
            }
        }
    }
}
