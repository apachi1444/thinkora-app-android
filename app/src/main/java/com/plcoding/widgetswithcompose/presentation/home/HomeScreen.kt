package com.plcoding.widgetswithcompose.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.widgetswithcompose.domain.model.Quote
import com.plcoding.widgetswithcompose.presentation.navigation.Screen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(padding) // No scaffold padding needed here as it's passed from MainScreen if we lifted it, but MainScreen passes padding to NavHost. 
            // Actually NavHost in MainScreen has modifier.padding(padding). So HomeScreen just fills max size.
            .background(Color(0xFFF8F9FC)) 
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        HomeHeader(userName = state.userName)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Quote of the Day",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        state.dailyQuote?.let { quote ->
            HeroQuoteCard(quote = quote)
        } ?: Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))

        CategoriesRow()

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Your Streak",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
         Spacer(modifier = Modifier.height(12.dp))
         StreakCard(streak = state.streak.currentStreak)
         
         if(state.habits.isNotEmpty()) {
             Spacer(modifier = Modifier.height(24.dp))
             Text(
                text = "Your Habits",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
             )
             Spacer(modifier = Modifier.height(12.dp))
             // Display first 3 items or so
             state.habits.take(3).forEach { habit ->
                 com.plcoding.widgetswithcompose.presentation.habits.HabitItem(habit = habit)
                 Spacer(modifier = Modifier.height(8.dp))
             }
         }
    }
}

@Composable
fun HomeHeader(userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Profile Image (Placeholder)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
               Icon(Icons.Default.Build, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Hi, $userName",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Let's get inspired!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
            IconButton(
                onClick = { },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    }
}

@Composable
fun HeroQuoteCard(quote: Quote) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF6366F1), // Indigo
                        Color(0xFF8B5CF6)  // Violet
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null, 
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Today",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = quote.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            
            Text(
                text = "\"${quote.content}\"",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                maxLines = 3
            )
            
            Text(
                text = "- ${quote.author}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun CategoriesRow() {
    val categories = listOf(
        "Business" to Color(0xFFE0E7FF),
        "Life" to Color(0xFFFCE7F3),
        "Sports" to Color(0xFFDCFCE7),
        "Tech" to Color(0xFFFEF3C7)
    )
    
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(categories.size) { index ->
            val (name, color) = categories[index]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name.take(1),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StreakCard(streak: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
         Row(
             modifier = Modifier.padding(16.dp),
             verticalAlignment = Alignment.CenterVertically
         ) {
             Box(
                 modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFEDD5)),
                 contentAlignment = Alignment.Center
             ) {
                 Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF97316))
             }
             Spacer(modifier = Modifier.width(16.dp))
             Column {
                 Text(
                     text = "$streak Day Streak!",
                     style = MaterialTheme.typography.titleMedium,
                     fontWeight = FontWeight.Bold
                 )
                 Text(
                     text = "Keep reading daily to build your habit.",
                     style = MaterialTheme.typography.bodySmall,
                     color = Color.Gray
                 )
             }
         }
    }
}

