package com.apachi.thinkora.feature.quotes

package com.apachi.thinkora.feature.quotes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.app.Activity
import com.apachi.thinkora.core.designsystem.component.ThinkoraTextField
import com.apachi.thinkora.core.designsystem.component.ThinkoraButton
import com.apachi.thinkora.core.designsystem.component.ThinkoraTopAppBar
import androidx.compose.ui.res.stringResource
import com.apachi.thinkora.R

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
    Scaffold(
        topBar = {
            ThinkoraTopAppBar(
                title = { Text(stringResource(R.string.add_quote_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
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
                label = { Text(stringResource(R.string.add_quote_content_label)) },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            ThinkoraTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(stringResource(R.string.add_quote_author_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Category Selection (Simple dropdown or chips)
            Text(stringResource(R.string.add_quote_category_label), style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val categories = listOf(
                    stringResource(R.string.add_quote_category_personal),
                    stringResource(R.string.add_quote_category_motivation),
                    stringResource(R.string.add_quote_category_work),
                    stringResource(R.string.add_quote_category_life)
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
                Text(stringResource(R.string.add_quote_save_button))
            }
        }
    }
}
