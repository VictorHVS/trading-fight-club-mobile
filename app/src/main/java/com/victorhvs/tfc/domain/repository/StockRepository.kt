package com.victorhvs.tfc.domain.repository

import androidx.paging.PagingData
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.enums.Interval
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.models.TimeSeries
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun searchStocks(query: String): Flow<PagingData<Stock>>

    fun fetchStock(uuid: String) : Flow<FirestoreState<Stock?>>

    fun fetchTimeSeries(stockId: String, interval: Interval) : Flow<FirestoreState<List<TimeSeries?>>>
}
