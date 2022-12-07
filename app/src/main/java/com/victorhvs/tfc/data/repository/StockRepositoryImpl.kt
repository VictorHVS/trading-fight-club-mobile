package com.victorhvs.tfc.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.data.datasource.FirebaseStocksPagingSource
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.repository.StockRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class StockRepositoryImpl @Inject constructor(
    private val client: FirebaseFirestore,
    private val dispatcher: DispatcherProvider
) : StockRepository {

    companion object {
        const val STOCKS_PER_PAGE = 40
    }
    override fun searchStocks(query: String): Flow<PagingData<Stock>> {
        return Pager(
            config = PagingConfig(pageSize = STOCKS_PER_PAGE),
            pagingSourceFactory = {
                FirebaseStocksPagingSource(
                    client = client,
                    query = query,
                    dispatcher = dispatcher,
                )
            },
        ).flow
    }
}
