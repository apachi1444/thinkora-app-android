package com.apachi.thinkora.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.apachi.thinkora.domain.navigation.Screen
import com.apachi.thinkora.ui.components.HeroQuoteCard
import com.apachi.thinkora.feature.habits.HabitItem

@Composable
fun HomeScreen(
    navController: NavController,
    onOpenDrawer: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        HomeHeader(
            userName = state.userName,
            onSearchClick = { navController.navigate(Screen.SearchScreen.route) },
            onNotificationClick = { navController.navigate(Screen.NotificationsScreen.route) },
            onOpenDrawer = onOpenDrawer
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Quote of the Day",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        state.dailyQuote?.let { quote ->
            HeroQuoteCard(
                quote = quote,
                onMarkRead = { viewModel.onEvent(HomeEvent.MarkAsRead) },
                onToggleFavorite = { viewModel.onEvent(HomeEvent.ToggleFavorite) }
            )
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

        CategoriesRow(
            onCategoryClick = { category ->
                navController.navigate(Screen.CategoryQuotesScreen.createRoute(category))
            }
        )

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
             state.habits.take(3).forEach { habit ->
                 HabitItem(
                     habit = habit,
                     onIncrementClick = { habitId ->
                         viewModel.onEvent(HomeEvent.IncrementHabitStreak(habitId))
                     }
                 )
                 Spacer(modifier = Modifier.height(8.dp))
             }
         }
    }
}

@Composable
fun HomeHeader(
    userName: String,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
             // Side Menu Icon
            IconButton(
                onClick = onOpenDrawer,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.common_menu))
            }

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
                    text = stringResource(R.string.home_greeting_name, userName),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.home_inspiration_sub),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = { onNotificationClick() },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = stringResource(R.string.common_notifications))
            }
            IconButton(
                onClick = { onSearchClick() },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Icon(Icons.Default.Search, contentDescription = stringResource(R.string.common_search))
            }
        }
    }
}


@Composable
fun CategoriesRow(
    onCategoryClick: (String) -> Unit
) {
    val categories = listOf(
        stringResource(R.string.category_business) to Color(0xFFE0E7FF),
        stringResource(R.string.category_life) to Color(0xFFFCE7F3),
        stringResource(R.string.category_sports) to Color(0xFFDCFCE7),
        stringResource(R.string.category_tech) to Color(0xFFFEF3C7)
    )
    
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(categories.size) { index ->
            val (name, color) = categories[index]
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCategoryClick(name) }) {
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
                     text = stringResource(R.string.home_streak_title, streak),
                     style = MaterialTheme.typography.titleMedium,
                     fontWeight = FontWeight.Bold
                 )
                 Text(
                     text = stringResource(R.string.home_streak_desc),
                     style = MaterialTheme.typography.bodySmall,
                     color = Color.Gray
                 )
             }
         }
    }
}

