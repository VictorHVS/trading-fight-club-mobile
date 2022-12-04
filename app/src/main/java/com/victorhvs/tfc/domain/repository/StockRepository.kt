package com.victorhvs.tfc.domain.repository

import com.victorhvs.tfc.core.State
import com.victorhvs.tfc.domain.models.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getStocks(): Flow<State<List<Stock>>>
}

