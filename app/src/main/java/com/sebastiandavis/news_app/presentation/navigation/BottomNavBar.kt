package com.sebastiandavis.news_app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem(
            name = "Home",
            route = Screen.Home.route,
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            name = "For You",
            route = Screen.Recommendations.route,
            icon = Icons.Default.Recommend
        ),
        BottomNavItem(
            name = "Saved",
            route = Screen.Saved.route,
            icon = Icons.Default.Bookmarks
        ),
        BottomNavItem(
            name = "Settings",
            route = Screen.Preferences.route,
            icon = Icons.Default.Settings
        )
    )
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    if (currentRoute !in listOf(Screen.ArticleDetail.route)) {
        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.name) },
                    label = { Text(text = item.name) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
} 