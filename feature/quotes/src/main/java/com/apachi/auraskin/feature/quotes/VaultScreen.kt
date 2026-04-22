package com.apachi.auraskin.feature.quotes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.apachi.auraskin.domain.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("The Vault") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Your daily dose of skin-positivity and self-care.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Explore Categories",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn {
                item {
                    VaultMenuItem(
                        title = "Skin Positivity",
                        onClick = { navController.navigate(Screen.CategoryQuotesScreen.createRoute("SKIN_POSITIVITY")) }
                    )
                }
                item {
                    VaultMenuItem(
                        title = "Stress Relief",
                        onClick = { navController.navigate(Screen.CategoryQuotesScreen.createRoute("STRESS_RELIEF")) }
                    )
                }
                item {
                    VaultMenuItem(
                        title = "Patience & Healing",
                        onClick = { navController.navigate(Screen.CategoryQuotesScreen.createRoute("PATIENCE")) }
                    )
                }
                item {
                    VaultMenuItem(
                        title = "My Custom Quotes",
                        onClick = { navController.navigate(Screen.CustomQuotesScreen.route) }
                    )
                }
            }
        }
    }
}

@Composable
fun VaultMenuItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate to $title"
            )
        }
    }
}
