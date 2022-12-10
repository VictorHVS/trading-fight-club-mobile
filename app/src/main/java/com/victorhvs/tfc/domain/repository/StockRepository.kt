package com.victorhvs.tfc.domain.repository

import androidx.paging.PagingData
import com.victorhvs.tfc.domain.models.FirestoreState
import com.victorhvs.tfc.domain.models.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun searchStocks(query: String): Flow<PagingData<Stock>>

    fun fetchStock(uuid: String) : Flow<FirestoreState<Stock?>>
}
