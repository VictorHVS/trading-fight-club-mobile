package com.victorhvs.tfc.domain.models

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Portfolio(
    @PropertyName("uuid") val uuid: String = "",
    @JvmField @ServerTimestamp @PropertyName("created_at")
    val createdAt: Date = Date(),
    @JvmField @ServerTimestamp @PropertyName("updated_at")
    val updatedAt: Date = Date(),
    @JvmField @PropertyName("amount")
    val amount: Int = 0,
    @JvmField @PropertyName("currency")
    val currency: String = "",
    @JvmField @PropertyName("currency_symbol")
    val currencySymbol: String = "",
    @JvmField @PropertyName("exchange_id")
    val exchangeId: String = "",
    @JvmField @PropertyName("stock_id")
    val stockId: String = "",
    @JvmField @PropertyName("total_spent")
    val totalSpent: Double = 0.0,
    @JvmField @PropertyName("medium_price")
    val mediumPrice: Double = 0.0,
    @JvmField @PropertyName("user_id")
    val userId: String = "",
    @JvmField @PropertyName("stock_url")
    val stockUrl: String = "",
)
