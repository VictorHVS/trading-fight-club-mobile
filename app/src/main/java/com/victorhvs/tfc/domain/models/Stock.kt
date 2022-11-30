package com.victorhvs.tfc.domain.models

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Stock(
    @PropertyName("uuid") val uuid: String = "",
    @JvmField @PropertyName("exchange_id")
    val exchangeId: String = ".SA",
    @JvmField @ServerTimestamp @PropertyName("created_at")
    val createdAt: Date = Date(),
    @JvmField @ServerTimestamp @PropertyName("updated_at")
    val updatedAt: Date = Date(),
    @JvmField @PropertyName("city")
    val city: String = "",
    @JvmField @PropertyName("country")
    val country: String = "",
    @JvmField @PropertyName("currency")
    val currency: String = "BRL",
    @JvmField @PropertyName("description")
    val description: String = "",
    @JvmField @PropertyName("enabled")
    val enabled: Boolean = true,
    @JvmField @PropertyName("exchange_name")
    val exchangeName: String = "BOVESPA",
    @JvmField @PropertyName("industry")
    val industry: String = "",
    @JvmField @PropertyName("logo_url")
    val logoUrl: String = "",
    @JvmField @PropertyName("name")
    val name: String = "",
    @JvmField @PropertyName("price")
    val price: Double = 0.0,
    @JvmField @PropertyName("price_absolute_flutuation")
    val priceAbsoluteFloating: Double = 0.0,
    @JvmField @PropertyName("price_flutuation")
    val priceFloating: Double = 0.0,
    @JvmField @PropertyName("sector")
    val sector: String = "",
    @JvmField @PropertyName("state")
    val state: String = "",
    @JvmField @PropertyName("symbol")
    val symbol: String = "",
    @JvmField @PropertyName("website")
    val website: String = "",
    @JvmField @PropertyName("week")
    val week: List<Double> = listOf(),
)
