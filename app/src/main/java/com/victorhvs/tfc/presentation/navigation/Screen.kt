package com.victorhvs.tfc.presentation.navigation

sealed class Screen(val route: String) {

    object ExploreScreen : Screen("explore")
    object StockDetailScreen : Screen("stock_screen")
}
