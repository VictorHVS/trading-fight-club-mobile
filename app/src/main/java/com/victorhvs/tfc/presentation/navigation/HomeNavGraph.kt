package com.victorhvs.tfc.presentation.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.victorhvs.tfc.presentation.screens.explore.ExploreScreen
import com.victorhvs.tfc.presentation.screens.home.BottomBarScreen
import com.victorhvs.tfc.presentation.screens.stock.StockScreen
import com.victorhvs.tfc.presentation.screens.stock.TfcBottomSheet
import kotlinx.coroutines.launch

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Wallet.route
    ) {
        composable(route = BottomBarScreen.Wallet.route) {
        }
        composable(route = BottomBarScreen.Explore.route) {
            ExploreScreen(navigateToStockScreen = { stock ->
                navController.navigate("${StockScreen.Detail.route}/${stock.uuid}")
            }
            )
        }
        composable(route = BottomBarScreen.Rank.route) {
        }
        composable(route = BottomBarScreen.Profile.route) {
        }
        detailsNavGraph(navController = navController)
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = "${StockScreen.Detail.route}/{stockId}"
    ) {
        composable(
            route = "${StockScreen.Detail.route}/{stockId}",
            arguments = listOf(
                navArgument("stockId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val stockId: String? = backStackEntry.arguments?.getString("stockId")
            if (stockId.isNullOrBlank()) {
                navController.popBackStack()
                return@composable
            }

            val skipHalfExpanded by remember { mutableStateOf(true) }
            val state = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = skipHalfExpanded
            )
            val scope = rememberCoroutineScope()

            ModalBottomSheetLayout(
                sheetState = state,
                sheetContent = {
                    TfcBottomSheet(
                        showFeed = {},
                        showAnotherSheet = {},
                        arg = "ae"
                    )
                }
            ) {

                StockScreen(
                    stockId = stockId.toString(),
                    stockName = "",
                    navigateBack = {
                        navController.popBackStack()
                    },
                    showSheet = {
                        scope.launch {
                            state.show()
                        }
                    }
                )
            }
        }
        composable(route = StockScreen.BuySell.route) {
        }
    }
}

sealed class StockScreen(val route: String) {
    object Detail : StockScreen(route = "DETAIL/{stock_id}")
    object BuySell : StockScreen(route = "BUY_SELL")
}
