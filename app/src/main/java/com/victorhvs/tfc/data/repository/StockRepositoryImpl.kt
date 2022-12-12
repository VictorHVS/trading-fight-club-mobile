package com.victorhvs.tfc.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.data.datasource.FirebaseStocksPagingSource
import com.victorhvs.tfc.data.extensions.getStatefulCollection
import com.victorhvs.tfc.data.extensions.observeStatefulCollection
import com.victorhvs.tfc.data.extensions.observeStatefulDoc
import com.victorhvs.tfc.domain.enums.FirestoreState
import com.victorhvs.tfc.domain.enums.Interval
import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.models.TimeSeries
import com.victorhvs.tfc.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

    override fun fetchStock(uuid: String) =
        observeStatefulDoc<Stock>(client.document("$STOCK_REF/$uuid"))

    override fun fetchTimeSeries(
        stockId: String,
        interval: Interval
    ): Flow<FirestoreState<List<TimeSeries?>>> {
        val path = "$STOCK_REF/$stockId/${interval.interval}"

        var query: Query = client.collection(path)

        interval.periodMilliseconds()?.let {
            query = client.collection(path).whereGreaterThanOrEqualTo("uuid", it)
        }

        return observeStatefulCollection(query)

    }

//    return withContext(dispatcher.io()) {
//        val snapshot = client.collection(FirebaseDataSourceImp.STOCK_REF)
//            .whereEqualTo("enabled", true)
//            .orderBy(FirebaseDataSourceImp.STOCK_ORDER_FIELD, Query.Direction.ASCENDING)
//            .limit(FirebaseDataSourceImp.STOCKS_PER_PAGE)
//            .get()
//            .await()
//
//        snapshot.toObjects(Stock::class.java)
//    }

}
