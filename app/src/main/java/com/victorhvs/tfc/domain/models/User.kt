package com.victorhvs.tfc.domain.models

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    @PropertyName("uuid") val uuid: String = "",
    @JvmField @ServerTimestamp @PropertyName("created_at")
    val createdAt: Date = Date(),
    @JvmField @PropertyName("week")
    val week: List<Double> = listOf(),
    @JvmField @PropertyName("name")
    val name: String = "",
    @JvmField @PropertyName("username")
    val username: String = "",
    @JvmField @PropertyName("performance")
    val performance: Double = 0.0,
    @JvmField @PropertyName("proftable")
    val proftable: Double = 0.0,
    @JvmField @PropertyName("trades")
    val trades: Int = 0,
    @JvmField @PropertyName("rank_position")
    val rankPosition: Int = 0,
    @JvmField @PropertyName("portfolio")
    val portfolio: UserPortfolio,
)

data class UserPortfolio(
    @JvmField @PropertyName("net_value")
    val netValue: Double = 0.0,
    @JvmField @PropertyName("price_flutuation")
    val priceFlutuation: Double = 0.0,
    @JvmField @PropertyName("price_flutuation_absolute")
    val priceFlutuationAbsolute: Double = 0.0,
    @JvmField @PropertyName("sum")
    val sum: Double = 0.0,
    @JvmField @PropertyName("symbol")
    val symbol: String = "R$"
)
