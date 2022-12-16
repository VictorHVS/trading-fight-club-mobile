package com.victorhvs.tfc.presentation.screens.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.victorhvs.tfc.R
import com.victorhvs.tfc.presentation.navigation.HomeNavGraph

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    logout: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController)}
    ) {
        print(it)
        HomeNavGraph(navController = navController, logout)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Wallet,
        BottomBarScreen.Explore,
        BottomBarScreen.Rank,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        Column {
            Divider(modifier = Modifier.fillMaxWidth())
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
    NavigationBarItem(
        label = {
            Text(text = stringResource(screen.title))
        },
        icon = {
            Icon(
                imageVector = if (selected) screen.iconFilled else screen.iconOutlined,
                contentDescription = stringResource(R.string.navigation_icon_description)
            )
        },
        selected = selected,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        }
    )
}

sealed class BottomBarScreen(
    val route: String,
    @StringRes val title: Int,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector
) {
    object Wallet : BottomBarScreen(
        route = "WALLET",
        title = R.string.screen_wallet,
        iconFilled = Icons.Rounded.PieChart,
        iconOutlined = Icons.Outlined.PieChart
    )

    object Explore : BottomBarScreen(
        route = "EXPLORE",
        title = R.string.screen_explore,
        iconFilled = Icons.Rounded.Search,
        iconOutlined = Icons.Outlined.Search
    )

    object Rank : BottomBarScreen(
        route = "RANK",
        title = R.string.screen_rank,
        iconFilled = Icons.Rounded.Groups,
        iconOutlined = Icons.Outlined.Groups
    )

    object Profile : BottomBarScreen(
        route = "PROFILE",
        title = R.string.screen_profile,
        iconFilled = Icons.Rounded.Person,
        iconOutlined = Icons.Outlined.Person,
    )
}