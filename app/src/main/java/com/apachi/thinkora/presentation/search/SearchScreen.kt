package com.apachi.thinkora.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }
    val categories = listOf("All", "Haircuts", "Make up", "Massage", "Skin care")
    var selectedCategory by remember { mutableStateOf("All") }

    Surface(
        color = Color(0xFFF8F9FC),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Search",
                style = MaterialTheme.typography.bodyLarge, // Adjust style as needed
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Search Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                 TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Salon", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { navController.popBackStack() }) {
                     Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF1E293B))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Categories
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { 
                            Text(
                                text = category, 
                                color = if (isSelected) Color.White else Color(0xFF94A3B8)
                            ) 
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF1E293B),
                            containerColor = Color.Transparent
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) Color(0xFF1E293B) else Color(0xFFE2E8F0)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Result found (246)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Results List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Dummy Data
                val results = listOf(
                    SearchResult("Green Apple", "6391 Elgin St. Celina, Delaware", 4.5, "15 km", "https://via.placeholder.com/150"),
                    SearchResult("Jawed Habib", "8502 Preston Rd. Inglewood, Maine", 3.8, "22 km", "https://via.placeholder.com/150"),
                    SearchResult("The Galleria", "4140 Parker Rd. Allentown, New Mexico", 5.0, "48 km", "https://via.placeholder.com/150"),
                    SearchResult("Michael Saldana", "3891 Ranchview Dr. Richardson, California", 4.2, "89 km", "https://via.placeholder.com/150"),
                    SearchResult("Fox and Jane", "3517 W. Gray St. Utica, Pennsylvania", 4.8, "106 km", "https://via.placeholder.com/150"),
                )

                items(results) { result ->
                     SearchResultItem(parseSearchResult(result))
                }
            }
        }
    }
}

data class SearchResult(
    val name: String,
    val address: String,
    val rating: Double,
    val distance: String,
    val imageUrl: String
)

// Helper to make dummy data more visual if needed, but for now we use placeholder images or colored boxes
@Composable
fun SearchResultItem(result: SearchResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
        ) {
              // Image would go here
              // Image(painter = rememberAsyncImagePainter(result.imageUrl), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = result.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = result.address,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.SpaceBetween,
                 modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB020),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    repeat(4) {
                         Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB020),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                     Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = result.distance,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF1E293B),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Fix missing LocationOn icon by adding import or finding alternative
// Assuming the user has material icons extended or we use a basic one. 
// I'll stick to basic icons available in Default for safety or assume extended is present.
// LocationOn is in extended usually. I'll check imports. If not I will use Place.
// Actually LocationOn is in Filled. 

fun parseSearchResult(s: SearchResult) : SearchResult {
    return s
}
