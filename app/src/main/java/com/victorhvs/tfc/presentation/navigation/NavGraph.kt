package com.victorhvs.tfc.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.victorhvs.tfc.data.fake.FakeDataSource
import com.victorhvs.tfc.presentation.screens.explore.ExploreScreen
import com.victorhvs.tfc.presentation.screens.stock.StockScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ExploreScreen.route,
    ) {
        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen(
                navigateToStockScreen = { stock ->
//                    val jsonStock = Gson().toJson(stock.copy(description = ""))
                    navController.navigate("${Screen.StockDetailScreen.route}/${stock.uuid}")
                },
            )
        }
        composable(
            route = "${Screen.StockDetailScreen.route}/{jsonStock}",
            arguments = listOf(
                navArgument("jsonStock") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
//            val jsonStock = backStackEntry.arguments?.getString("stockId") ?: ""
//            val stock = Gson().fromJson(jsonStock, Stock::class.java)
            StockScreen(
                stock = FakeDataSource.flry3,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
