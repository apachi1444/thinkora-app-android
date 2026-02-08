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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.apachi.thinkora.feature.home.HomeScreen
import com.apachi.thinkora.presentation.navigation.Screen
import com.apachi.thinkora.feature.settings.SettingsScreen
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    rootNavController: NavHostController
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isDrawerOpen by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }


    com.apachi.thinkora.feature.drawer.ZoomDrawer(
        isDrawerOpen = isDrawerOpen,
        onCloseDrawer = { isDrawerOpen = false },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            com.apachi.thinkora.feature.drawer.DrawerContent(
                onLogoutClick = { /* TODO: Handle logout */ },
                onCloseDrawer = {
                    isDrawerOpen = false
                }
            )
        }
    ) {
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
                composable(
                    route = Screen.HomeScreen.route,
                    deepLinks = listOf(androidx.navigation.navDeepLink { uriPattern = "thinkora://app/home" })
                ) {
                    HomeScreen(
                        navController = bottomNavController,
                        onOpenDrawer = { isDrawerOpen = true }
                    )
                }
                composable(
                    route = Screen.HabitsScreen.route,
                    deepLinks = listOf(androidx.navigation.navDeepLink { uriPattern = "thinkora://app/habits" })
                ) {
                    com.apachi.thinkora.feature.habits.HabitsScreen()
                }
                composable(
                    route = Screen.SettingsScreen.route,
                    deepLinks = listOf(androidx.navigation.navDeepLink { uriPattern = "thinkora://app/settings" })
                ) {
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
                    com.apachi.thinkora.feature.category.CategoryQuotesScreen(navController = bottomNavController)
                }
                composable(
                    route = Screen.SearchScreen.route,
                    deepLinks = listOf(androidx.navigation.navDeepLink { uriPattern = "thinkora://app/search" })
                ) {
                    com.apachi.thinkora.feature.search.SearchScreen(navController = bottomNavController)
                }
                composable(
                    route = Screen.NotificationsScreen.route,
                    deepLinks = listOf(androidx.navigation.navDeepLink { uriPattern = "thinkora://app/notifications" })
                ) {
                    com.apachi.thinkora.feature.notifications.NotificationsScreen(navController = bottomNavController)
                }
            }
        }
    }
}
