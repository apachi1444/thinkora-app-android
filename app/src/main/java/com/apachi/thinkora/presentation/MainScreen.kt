package com.apachi.thinkora.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apachi.thinkora.presentation.home.HomeScreen
import com.apachi.thinkora.presentation.navigation.Screen
import com.apachi.thinkora.presentation.settings.SettingsScreen


@Composable
fun MainScreen(
    rootNavController: NavHostController
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentRoute == Screen.HomeScreen.route,
                    onClick = { 
                        bottomNavController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.HomeScreen.route) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.HabitsScreen.route,
                    onClick = { 
                        bottomNavController.navigate(Screen.HabitsScreen.route) {
                            popUpTo(Screen.HomeScreen.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Habits") },
                    label = { Text("Habits") }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.SettingsScreen.route,
                    onClick = { 
                         bottomNavController.navigate(Screen.SettingsScreen.route) {
                            popUpTo(Screen.HomeScreen.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.HomeScreen.route) {
                HomeScreen(navController = bottomNavController)
            }
            composable(Screen.HabitsScreen.route) {
                com.apachi.thinkora.presentation.habits.HabitsScreen()
            }
            composable(Screen.SettingsScreen.route) {
                SettingsScreen()
            }
            composable(
                route = Screen.CategoryQuotesScreen.route,
                arguments = listOf(
                    androidx.navigation.navArgument("categoryName") {
                        type = androidx.navigation.NavType.StringType
                    }
                )
            ) {
                com.apachi.thinkora.presentation.category.CategoryQuotesScreen(navController = bottomNavController)
            }
        }
    }
}
