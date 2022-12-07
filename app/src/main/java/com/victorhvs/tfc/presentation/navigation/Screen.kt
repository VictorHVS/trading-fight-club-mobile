package com.victorhvs.tfc.presentation.navigation

sealed class Screen(val route: String) {

    object ExploreScreen : Screen("explore")
    object StockDetailScreen : Screen("detail_screen/{stockId}") {
        fun passStockId(stockId: String): String {
            return "detail_screen/$stockId"
        }
    }
}
