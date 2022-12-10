package com.victorhvs.tfc.domain.models

import com.google.firebase.firestore.PropertyName

data class TimeSeries(
    @PropertyName("uuid") val uuid: String = "",
    @JvmField @PropertyName("stock_uuid") val stockUuid: String? = null,
    @JvmField @PropertyName("close") val close: Double = 0.0,
    @JvmField @PropertyName("open") val open: Double = 0.0,
    @JvmField @PropertyName("high") val high: Double = 0.0,
    @JvmField @PropertyName("low") val low: Double = 0.0,
    @JvmField @PropertyName("volume") val volume: Double = 0.0,
    @JvmField @PropertyName("timezone") val timezone: String? = null,
    @JvmField @PropertyName("interval") val interval: String = "",
    @JvmField @PropertyName("exchange_uuid") val exchangeUuid: String = "",
    @JvmField @PropertyName("currency") val currency: String = "",
    @JvmField @PropertyName("datetime") val datetime: String = ""
)
