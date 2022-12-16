package com.victorhvs.tfc.domain.models

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Order(
    @PropertyName("uuid") val uuid: String = "",
    @JvmField @ServerTimestamp @PropertyName("created_at")
    val createdAt: Date = Date(),
    @JvmField @PropertyName("amount")
    val amount: Int = 0,
    @JvmField @PropertyName("currency")
    val currency: String = "",
    @JvmField @PropertyName("currency_symbol")
    val currencySymbol: String = "",
    @JvmField @PropertyName("exchange_id")
    val exchangeId: String = "",
    @JvmField @PropertyName("executed")
    val executed: Boolean = false,
    @JvmField @PropertyName("is_buy")
    val isBuy: Boolean = true,
    @JvmField @PropertyName("stock_id")
    val stockId: String = "",
    @JvmField @PropertyName("total_price")
    val totalPrice: Double = 0.0,
    @JvmField @PropertyName("unit_price")
    val unitPrice: Double = 0.0,
    @JvmField @PropertyName("user_id")
    val userId: String = "",
    @JvmField @PropertyName("stock_url")
    val stockUrl: String? = "",
)
