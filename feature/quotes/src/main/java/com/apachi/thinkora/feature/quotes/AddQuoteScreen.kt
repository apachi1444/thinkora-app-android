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
    
    val personalLabel = stringResource(R.string.cat_personal)
    var category by remember { mutableStateOf(personalLabel) }

    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            ThinkoraTopAppBar(
                title = { Text(stringResource(R.string.quotes_add_quote)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back)
                        )
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
                label = { Text(stringResource(R.string.quotes_content_label)) },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            ThinkoraTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(stringResource(R.string.quotes_author_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Category Selection
            Text(
                text = stringResource(R.string.quotes_category_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val categories = listOf(
                    stringResource(R.string.cat_personal),
                    stringResource(R.string.cat_motivation),
                    stringResource(R.string.cat_work),
                    stringResource(R.string.cat_life)
                )
                categories.forEach { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val defaultAuthor = stringResource(R.string.quotes_default_author)
            ThinkoraButton(
                onClick = {
                    if (content.isNotBlank()) {
                        viewModel.addQuote(content, author.ifBlank { defaultAuthor }, category, activity) {
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = content.isNotBlank()
            ) {
                Text(stringResource(R.string.quotes_save))
            }
        }
    }
}
