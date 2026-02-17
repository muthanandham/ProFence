package com.profence.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.profence.app.ui.ProFenceViewModel
import com.profence.app.ui.dashboard.DashboardScreen
import com.profence.app.ui.map.MapScreen
import com.profence.app.ui.logs.LogsScreen

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Dashboard : Screen("dashboard", "Active", Icons.Default.List)
    object Map : Screen("map", "Map", Icons.Default.Map)
    object Logs : Screen("logs", "Logs", Icons.Default.Warning)
}

@Composable
fun AppNavigation(viewModel: ProFenceViewModel) {
    val navController = rememberNavController()
    val screens = listOf(Screen.Dashboard, Screen.Map, Screen.Logs)

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel = viewModel, onNavigateToMap = {
                    navController.navigate(Screen.Map.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
            composable(Screen.Map.route) {
                MapScreen(viewModel = viewModel)
            }
            composable(Screen.Logs.route) {
                LogsScreen(viewModel = viewModel)
            }
        }
    }
}
