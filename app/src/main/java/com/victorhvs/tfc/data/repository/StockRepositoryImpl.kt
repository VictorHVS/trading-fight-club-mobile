package com.victorhvs.tfc.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.data.datasource.FirebaseStocksPagingSource
import com.victorhvs.tfc.data.extensions.getDoc
import com.victorhvs.tfc.data.extensions.observeStatefulDoc
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.repository.StockRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class StockRepositoryImpl @Inject constructor(
    private val client: FirebaseFirestore,
    private val dispatcher: DispatcherProvider
) : StockRepository {

    companion object {
        const val STOCK_REF = "stocks"
        const val STOCK_ORDER_FIELD = "uuid"
        const val STOCKS_PER_PAGE = 20
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

    override fun fetchStock(uuid: String) = observeStatefulDoc<Stock>(Firebase.firestore.document("$STOCK_REF/$uuid"))

}
