package com.victorhvs.tfc.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.victorhvs.tfc.presentation.screens.stocklist.ExploreScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Explore.route
    ) {
        composable(route = Screen.Explore.route) {
            ExploreScreen()
        }
    }
}
