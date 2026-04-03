package com.apachi.auraskin.feature.quotes


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
import com.apachi.auraskin.core.designsystem.component.AuraTextField
import com.apachi.auraskin.core.designsystem.component.AuraButton
import com.apachi.auraskin.core.designsystem.component.AuraTopAppBar


import androidx.compose.ui.res.stringResource
import com.apachi.auraskin.designsystem.R as DesignR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuoteScreen(
    navController: NavController,
    viewModel: CustomQuotesViewModel
) {
    var content by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    
    val personalLabel = stringResource(DesignR.string.cat_personal)
    var category by remember { mutableStateOf(personalLabel) }

    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            AuraTopAppBar(
                title = { Text(stringResource(DesignR.string.quotes_add_quote)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(DesignR.string.common_back)
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
            AuraTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(DesignR.string.quotes_content_label)) },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            AuraTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(stringResource(DesignR.string.quotes_author_label)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Category Selection
            Text(
                text = stringResource(DesignR.string.quotes_category_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val categories = listOf(
                    stringResource(DesignR.string.cat_personal),
                    stringResource(DesignR.string.cat_motivation),
                    stringResource(DesignR.string.cat_work),
                    stringResource(DesignR.string.cat_life)
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

            val defaultAuthor = stringResource(DesignR.string.quotes_default_author)
            AuraButton(
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
                Text(stringResource(DesignR.string.quotes_save))
            }
        }
    }
}
