package com.victorhvs.tfc.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.victorhvs.tfc.core.DispatcherProvider
import com.victorhvs.tfc.domain.models.Stock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface FirebaseDataSource {
    suspend fun getStocks(): List<Stock>
}

class FirebaseDataSourceImp @Inject constructor(
    val client: FirebaseFirestore,
    val dispatcher: DispatcherProvider
) : FirebaseDataSource {

    companion object {
        const val STOCK_REF = "stocks"
        const val STOCK_ORDER_FIELD = "uuid"
        const val STOCKS_PER_PAGE = 20L
    }

    override suspend fun getStocks(): List<Stock> {
        return withContext(dispatcher.io()) {
            val snapshot = client.collection(STOCK_REF)
                .whereEqualTo("enabled", true)
                .orderBy(STOCK_ORDER_FIELD, Query.Direction.ASCENDING)
                .limit(STOCKS_PER_PAGE)
                .get()
                .await()

            snapshot.toObjects(Stock::class.java)
        }
    }
}
