package com.victorhvs.tfc.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.victorhvs.tfc.presentation.screens.explore.ExploreScreen
import com.victorhvs.tfc.presentation.screens.home.BottomBarScreen
import com.victorhvs.tfc.presentation.screens.profile.ProfileScreen
import com.victorhvs.tfc.presentation.screens.ranking.ContestResultListScreen
import com.victorhvs.tfc.presentation.screens.stock.StockScreen
import com.victorhvs.tfc.presentation.screens.buy_sell.TfcBottomSheet
import kotlinx.coroutines.launch

@Composable
fun HomeNavGraph(navController: NavHostController, logout: () -> Unit) {
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
            ContestResultListScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navigateToAuthScreen = {
                logout()
            })
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
            val isBuy = remember { mutableStateOf(true) }

            when(state.currentValue) {
                ModalBottomSheetValue.Hidden -> {
                    LocalSoftwareKeyboardController.current?.hide()
                }
                ModalBottomSheetValue.Expanded -> {}
                ModalBottomSheetValue.HalfExpanded -> {}
            }

            ModalBottomSheetLayout(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.scrim),
                sheetState = state,
                sheetContent = {
                    TfcBottomSheet(
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                                color = MaterialTheme.colorScheme.surface
                            )
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        stockId = stockId,
                        isBuy = isBuy.value,
                        closeModal = {
                            scope.launch {
                                state.hide()
                            }
                        }
                    )
                }
            ) {

                StockScreen(
                    stockId = stockId.toString(),
                    navigateBack = {
                        navController.popBackStack()
                    },
                    showSheet = {
                        isBuy.value = it
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
