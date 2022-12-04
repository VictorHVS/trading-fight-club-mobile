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
        startDestination = Screen.ExploreScreen.route,
    ) {
        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen(
                navigateToStockScreen = { _ ->
//                    val jsonStock = Gson().toJson(stock)
//                    navController.navigate("${StockDetailScreen.route}/${jsonStock}")
                }
            )
        }
//        composable(
//            route = "${StockDetailScreen.route}/{jsonStock}",
//            arguments = listOf(
//                navArgument("jsonStock") {
//                    type = NavType.StringType
//                }
//            )
//        ) { backStackEntry ->
//            val jsonStock = backStackEntry.arguments?.getString("jsonStock") ?: ""
//            val product = Gson().fromJson(jsonStock, Stock::class.java)
//            StockDetailScreen(
//                product = product,
//                navigateBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
    }
}
