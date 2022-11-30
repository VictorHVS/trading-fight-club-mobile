package com.victorhvs.tfc.presentation.navigation

sealed class Screen(val route: String) {

    object Explore : Screen("explore")
    object StockDetail : Screen("detail_screen/{stockId}") {
        fun passStockId(stockId: String): String {
            return "detail_screen/$stockId"
        }
    }
}
