package com.apachi.auraskin.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.apachi.auraskin.data.ads.AdManager
import com.apachi.auraskin.designsystem.R
import com.apachi.auraskin.domain.navigation.Screen
import com.apachi.auraskin.feature.category.CategoryQuotesScreen
import com.apachi.auraskin.feature.drawer.DrawerContent
import com.apachi.auraskin.feature.drawer.ZoomDrawer
import com.apachi.auraskin.feature.habits.HabitsScreen
import com.apachi.auraskin.feature.home.HomeScreen
import com.apachi.auraskin.feature.settings.SettingsScreen


@Composable
fun MainScreen(
    rootNavController: NavHostController,
    adManager: AdManager
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isDrawerOpen by remember { mutableStateOf(false) }


    ZoomDrawer(
        isDrawerOpen = isDrawerOpen,
        onCloseDrawer = { isDrawerOpen = false },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            DrawerContent(
                onLogoutClick = { /* TODO: Handle logout */ },
                onAchievementsClick = {
                    isDrawerOpen = false
                    bottomNavController.navigate(Screen.AchievementsScreen.route)
                },
                onAnalyticsClick = {
                    isDrawerOpen = false
                    bottomNavController.navigate(Screen.AnalyticsScreen.route)
                }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // BannerAd(adManager = adManager)
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = currentRoute == Screen.HomeScreen.route,
                            onClick = {
                                bottomNavController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(Screen.HomeScreen.route) { inclusive = true }
                                }
                            },
                            icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.nav_home)) },
                            label = { Text(stringResource(R.string.nav_home)) }
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
                            icon = { Icon(Icons.Default.DateRange, contentDescription = stringResource(R.string.nav_habits)) },
                            label = { Text(stringResource(R.string.nav_habits)) }
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
                            icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.nav_settings)) },
                            label = { Text(stringResource(R.string.nav_settings)) }
                        )
                    }
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
                    deepLinks = listOf(navDeepLink { uriPattern = "thinkora://app/home" })
                ) {
                    HomeScreen(
                        navController = bottomNavController,
                        onOpenDrawer = { isDrawerOpen = true }
                    )
                }
                composable(
                    route = Screen.HabitsScreen.route,
                    deepLinks = listOf(navDeepLink { uriPattern = "thinkora://app/habits" })
                ) {
                    HabitsScreen()
                }
                composable(
                    route = Screen.SettingsScreen.route,
                    deepLinks = listOf(navDeepLink { uriPattern = "thinkora://app/settings" })
                ) {
                    SettingsScreen(navController = bottomNavController)
                }
                composable(
                    route = Screen.CategoryQuotesScreen.route,
                    arguments = listOf(
                        navArgument("categoryName") {
                            type = NavType.StringType
                        }
                    )
                ) {
                    CategoryQuotesScreen(navController = bottomNavController)
                }
                composable(
                    route = Screen.SearchScreen.route,
                    deepLinks = listOf(navDeepLink { uriPattern = "thinkora://app/search" })
                ) {
                    com.apachi.auraskin.feature.search.SearchScreen(navController = bottomNavController)
                }
                composable(
                    route = Screen.NotificationsScreen.route,
                    deepLinks = listOf(navDeepLink { uriPattern = "thinkora://app/notifications" })
                ) {
                    com.apachi.auraskin.feature.notifications.NotificationsScreen(navController = bottomNavController)
                }
                composable(
                    route = Screen.AchievementsScreen.route,
                    deepLinks = listOf(navDeepLink { uriPattern = "thinkora://app/achievements" })
                ) {
                    com.apachi.auraskin.feature.gamification.AchievementsScreen(navController = bottomNavController)
                }
                composable(route = Screen.CustomQuotesScreen.route) {
                    com.apachi.auraskin.feature.quotes.CustomQuotesScreen(navController = bottomNavController)
                }
                composable(route = Screen.AddQuoteScreen.route) {
                    com.apachi.auraskin.feature.quotes.AddQuoteScreen(navController = bottomNavController, viewModel = androidx.hilt.navigation.compose.hiltViewModel())
                }
                composable(route = Screen.AnalyticsScreen.route) {
                    com.apachi.auraskin.feature.analytics.AnalyticsScreen(navController = bottomNavController)
                }
            }
        }
    }
}
